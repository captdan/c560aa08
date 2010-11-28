Public Class Linker
    Dim filesToBeLinked As New ArrayList
    Dim errorsArray As New ArrayList
    Dim outputFile As String
    Dim currentLine As Integer
    Dim currentObjectFile As String

    Private Sub Button1_Click(ByVal sender As System.Object, ByVal e As System.EventArgs) Handles Button1.Click
        OpenFileDialog1.Title = "Open Object File to be Linked"
        OpenFileDialog1.ShowDialog()

        If My.Computer.FileSystem.FileExists(OpenFileDialog1.FileName) Then
            ListView2.Items.Add(New ListViewItem(OpenFileDialog1.FileName))
            filesToBeLinked.Add(OpenFileDialog1.FileName)
            ListView2.AutoResizeColumns(ColumnHeaderAutoResizeStyle.ColumnContent)
        End If
    End Sub

    Private Sub TabPage1_Click(ByVal sender As System.Object, ByVal e As System.EventArgs) Handles TabPage1.Click

    End Sub

    Private Sub Linker_Load(ByVal sender As System.Object, ByVal e As System.EventArgs) Handles MyBase.Load
        ListView1.View = View.Details
        ListView1.FullRowSelect = True
        ListView1.Columns.Add("#")
        ListView1.Columns.Add("Error Msg")
        ListView1.Columns.Add("Suggested Fix")
        ListView1.Columns.Add("File")
        ListView1.Columns.Add("Line #")

        ListView2.View = View.Details
        ListView2.FullRowSelect = True
        ListView2.Columns.Add("File")

        'Global Symbol Table 
        'Symbol| Loc assigned by Assembler | Loc assigned by Linker | Length of Module / program
    End Sub

    Private Sub Button3_Click(ByVal sender As System.Object, ByVal e As System.EventArgs) Handles Button3.Click
        SaveFileDialog1.Title = "Select a File to Output the Linked Object"
        SaveFileDialog1.ShowDialog()

        If SaveFileDialog1.FileName <> "" Then
            outputFile = SaveFileDialog1.FileName
            TextBox1.Text = SaveFileDialog1.FileName
        End If
    End Sub

    Private Sub Button2_Click(ByVal sender As System.Object, ByVal e As System.EventArgs) Handles Button2.Click
        ListView1.Items.Clear()
        Dim objectFileArray As New ArrayList
        If outputFile <> "" And filesToBeLinked.Count > 0 Then
            For Each fileName As String In filesToBeLinked
                objectFileArray.Add(createObjectFile(fileName))
            Next
            If ListView1.Items.Count = 0 Then
                checkExtRecords(objectFileArray)
                If ListView1.Items.Count = 0 Then
                    createLinkerFile(objectFileArray)
                Else
                    MsgBox("Linking could not occur due to errors")
                End If
            Else
                MsgBox("Linking could not occur due to errors")
            End If
        Else
            MsgBox("Make sure you select a file to be output and at least one file to be linked")
        End If
    End Sub
    Private Sub checkExtRecords(ByVal objectFileArray As ArrayList)
        ' This not only checks EXT records it also grabs the value from the other object file and ads it to 
        ' a field called labelvalue from the other object file.
        For Each y As ObjectFile In objectFileArray
            Dim tempObjectFileArray As New ArrayList(objectFileArray)
            tempObjectFileArray.Remove(y)
            For Each x As TextRecord In y.textRecords
                For Each z As Adjustment In x.adjustments
                    If z.type = Adjustment.Types.E Then
                        If linkingLabelExistsInObject(z.label, tempObjectFileArray) Then
                            If ListView1.Items.Count = 0 Then
                                z.labelValue = returnLinkingLabelValue(z.label, tempObjectFileArray)
                            End If
                        Else
                            addError(0, "No ENT Linking Record Found For EXT Label " & z.label, "Check Linking Records")
                        End If
                    End If
                Next
            Next
        Next

    End Sub
    Private Sub createLinkerFile(ByRef objectFileArray As ArrayList)
        Dim totalProgramLength As Integer = 0
        Dim currentOffSet As Integer = 0
        Dim linkerFileText As String = ""
        Dim tempValue As Integer = 0
        Dim recordCounter As Integer = 0
        For Each x As ObjectFile In objectFileArray
            totalProgramLength = totalProgramLength + x.programLength
        Next
        If totalProgramLength > 65536 Then
            addError(0, "The programs length after the files are linked is too large.", "Make programs smaller")
        Else
            linkerFileText = linkerFileText & "LH" & "|" & padZeros(Convert.ToString((TryCast(objectFileArray(0), ObjectFile).executionStartAddress - TryCast(objectFileArray(0), ObjectFile).programLoadPoint), 16), 4) & "|"
            linkerFileText = linkerFileText & TryCast(objectFileArray(0), ObjectFile).programName & "|"
            linkerFileText = linkerFileText & padZeros(Convert.ToString(totalProgramLength, 16), 4) & "|" & "0000|"
            linkerFileText = linkerFileText & Date.Now.ToString("yyyy:ddd") & "|"
            linkerFileText = linkerFileText & Date.Now.ToString("hh:mm:ss") & "|"
            linkerFileText = linkerFileText & "SAL-LINK|"
            linkerFileText = linkerFileText & TryCast(objectFileArray(0), ObjectFile).version & "|"
            linkerFileText = linkerFileText & TryCast(objectFileArray(0), ObjectFile).revision & "|"
            linkerFileText = linkerFileText & TryCast(objectFileArray(0), ObjectFile).programName

            linkerFileText = linkerFileText & vbCrLf

            For Each x As ObjectFile In objectFileArray
                For Each y As TextRecord In x.textRecords
                    If y.address >= 0 Then
                        recordCounter = recordCounter + 1

                        linkerFileText = linkerFileText & "LT" & "|"
                        If y.debug = False Then
                            linkerFileText = linkerFileText & "N" & "|"
                        Else
                            linkerFileText = linkerFileText & "Y" & "|"
                        End If

                        linkerFileText = linkerFileText & padZeros(Convert.ToString((y.address - x.programLoadPoint + currentOffSet), 16), 4) & "|"
                        tempValue = y.data
                        For Each z As Adjustment In y.adjustments
                            If z.type = Adjustment.Types.E Then
                                If z.action = Adjustment.Actions.Plus Then
                                    tempValue = tempValue + z.labelValue
                                ElseIf z.action = Adjustment.Actions.Minus Then
                                    tempValue = tempValue - z.labelValue
                                End If
                            End If
                        Next
                        linkerFileText = linkerFileText & padZeros(Convert.ToString(tempValue, 16), 8) & "|"
                        linkerFileText = linkerFileText & x.programName
                        linkerFileText = linkerFileText & vbCrLf
                    End If
                Next
                currentOffSet = currentOffSet + x.programLength
            Next



            linkerFileText = linkerFileText & "LE" & "|"
            linkerFileText = linkerFileText & padZeros(Convert.ToString((recordCounter + 2), 16), 4) & "|"
            linkerFileText = linkerFileText & TryCast(objectFileArray(0), ObjectFile).programName

            My.Computer.FileSystem.WriteAllText(outputFile, linkerFileText, False)
        End If

    End Sub
    Private Function padZeros(ByVal stringValue As String, ByVal numberOfDigits As Integer) As String
        Dim returnString As String = stringValue
        Dim tempCounter As Integer = 0
        'MsgBox("# digits: " & numberOfDigits & vbCrLf & returnString.Length)
        While tempCounter < (numberOfDigits - stringValue.Length)
            'MsgBox(returnString & vbCrLf & tempCounter)
            returnString = returnString.Insert(0, "0")
            tempCounter = tempCounter + 1
        End While
        'MsgBox(returnString)
        Return returnString
    End Function

    Private Function linkingLabelExistsInObject(ByVal labelValue As String, ByRef objectFileArray As ArrayList) As Boolean
        Dim exists As Boolean = False
        For Each y As ObjectFile In objectFileArray
            For Each x As LinkingRecord In y.linkingRecords
                If x.name = labelValue Then
                    If exists = True Then
                        addError(0, "Duplicate ENT Linking Records Found For Label " & labelValue, "Check Linking Records")
                    End If
                    exists = True
                End If
            Next
        Next

        Return exists
    End Function
    Private Function returnLinkingLabelValue(ByVal labelValue As String, ByRef objectFileArray As ArrayList) As Integer
        Dim value As Integer = 0
        Dim found As Boolean = False

        For Each y As ObjectFile In objectFileArray
            For Each x As LinkingRecord In y.linkingRecords
                If x.name = labelValue Then
                    For Each z As TextRecord In y.textRecords
                        If z.address = x.address Then
                            value = z.data
                            found = True
                        End If
                    Next
                End If
            Next
        Next

        If found = False Then
            addError(0, "Linking Label  " & labelValue & " did not correspond to a correct address ", "Check Linking Records Address")
        End If

        Return value
    End Function
    Private Function createObjectFile(ByVal fileName As String) As ObjectFile
        Dim fileContentArray As Array = Split(My.Computer.FileSystem.ReadAllText(fileName), vbCrLf)
        Dim hasHeader As Boolean = False
        Dim hasEnd As Boolean = False
        Dim fatalError As Boolean = False
        Dim counter As Integer = 1
        currentObjectFile = fileName
        currentLine = counter
        Dim objectFile As New ObjectFile
        For Each line As String In fileContentArray
            If line <> "" Then
                Dim lineArray As Array = Split(line, "|")
                If lineArray.Length > 1 And fatalError = False Then
                    If lineArray(0) = "H" And hasHeader = False Then
                        If lineArray.Length = 13 Then
                            parseHeader(lineArray, objectFile, fatalError)
                            If fatalError <> True Then
                                hasHeader = True
                            End If
                        Else
                            addError(0, "Header Line does not have enough Fields", "Check Header Record Format")
                        End If
                    ElseIf hasHeader = True Then
                        If lineArray(0) = "L" Then
                            If lineArray.Length = 5 Then
                                parseLinkRecord(lineArray, objectFile, fatalError)
                            Else
                                addError(0, "Linking Record Line does not have enough Fields", "Check Linking Record Format")
                            End If
                        ElseIf lineArray(0) = "T" Then
                            If lineArray.Length > 5 Then
                                parseTextRecord(lineArray, objectFile, fatalError)
                            Else
                                addError(0, "Text Record Line does not have enough Fields", "Check Text Record Format")
                            End If
                        ElseIf lineArray(0) = "E" Then
                            If lineArray.Length = 3 Then
                                parseEndRecord(lineArray, objectFile, fatalError)
                                hasEnd = True
                            Else
                                addError(0, "End Record Line does not have the correct amount of Fields", "Check End Record Format")
                            End If
                        Else
                            addError(0, "Invalid Line Found in Object File", "Check Header Record Format")
                        End If
                    Else
                        addError(0, "No Header Found in at Start of Object File", "Check line for errors")
                        fatalError = True
                    End If
                Else
                    If lineArray.Length > 1 = False Then
                        addError(0, "Invalid Line Found in Object File", "Check line for errors")
                    End If
                    addError(0, "Fatal Error Exiting Linking", "Check Previous Errors")
                    Exit For
                End If
            End If
            counter = counter + 1
            currentLine = counter
        Next
        If hasHeader = False Then
            addError(0, "File is missing header record.", "Check Input File Header Record")
        End If
        If hasEnd = False Then
            addError(0, "File is missing end record.", "Check Input File End Record")
        End If
        If objectFile.numberOfLinkingRecords <> objectFile.linkingRecords.Count Then
            addError(0, "The Number of Linking Records does not match the actual amount of linking records in the header record.", "Check the number of linking records")
        End If
        If objectFile.numberOfTextRecords <> objectFile.textRecords.Count Then
            addError(0, "The Number of Text Records does not match the actual amount of linking records in the header record.", "Check the number of header records")
        End If
        Dim tempValueArray As New ArrayList
        For x As Integer = objectFile.programLoadPoint + 1 To (objectFile.programLoadPoint + objectFile.programLength - 2)
            tempValueArray.Add(x)
        Next
        For Each x As TextRecord In objectFile.textRecords
            If tempValueArray.Contains(x.address) Then
                tempValueArray.Remove(x.address)
            Else
                addError(0, "Address: " & padZeros(Convert.ToString(x.address, 16), 4) & " is not within the program load and length range for a text record", "Check Address in Object File")
            End If
        Next
        If tempValueArray.Count <> 0 Then
            For Each x As Integer In tempValueArray
                addError(0, "Address: " & padZeros(Convert.ToString(x, 16), 4) & " Not found in program, but should be included within Object File", "Check Address in Object File")
            Next
        End If

        currentLine = 0

        Return objectFile
    End Function

    Private Sub parseEndRecord(ByVal lineArray As Array, ByRef objectFile As ObjectFile, ByRef fatalError As Boolean)
        Dim tempValue As Integer
        Try
            tempValue = Convert.ToInt32(lineArray(1), 16)
            If (2 + objectFile.linkingRecords.Count + objectFile.textRecords.Count) = tempValue Then

            Else
                addError(0, "Number of total records in End Record do not match, expected " & (2 + objectFile.linkingRecords.Count + objectFile.textRecords.Count) & " recieved " & tempValue, "Check End Record")
                fatalError = True
            End If
        Catch ex As Exception
            addError(0, "Could not parse Total Number of Records in End Record", "Check Number of End Records")
            fatalError = True
        End Try

        If lineArray(2) <> objectFile.programName Then
            addError(0, "End Record Program Name does not match program name", "Check End Record Program Name")
        End If

    End Sub

    Private Sub parseTextRecord(ByVal lineArray As Array, ByRef objectFile As ObjectFile, ByRef fatalError As Boolean)
        Dim textRecord As New TextRecord
        Dim tempValue As Integer
        Try
            tempValue = Convert.ToInt32(lineArray(1), 16)
            If lineArray(1).ToString.Length <= 4 And tempValue >= 0 Then
                If tempValue >= objectFile.programLoadPoint And tempValue <= (objectFile.programLoadPoint + objectFile.programLength) Then
                    textRecord.address = tempValue
                Else
                    addError(0, "Text Record Address outside of the program range", "Check Text Record Address Range")
                    fatalError = True
                End If
            Else
                addError(0, "Could not parse Address of Text Record", "Check Text Record Address")
                fatalError = True
            End If
        Catch ex As Exception
            If lineArray(1).ToString.ToUpper = "XXXX" Then
                textRecord.address = -1
            Else
                addError(0, "Could not parse Address of Text Record", "Check Text Record Address")
                fatalError = True
            End If
        End Try

        If lineArray(2).ToString.ToUpper = "Y" Then
            textRecord.debug = True
        ElseIf lineArray(2).ToString.ToUpper = "N" Then
            textRecord.debug = False
        Else
            addError(0, "Could not parses Debug Value of Text Record", "Check Text Record Debug Field")
        End If

        Try
            tempValue = Convert.ToInt32(lineArray(3), 16)
            If lineArray(3).ToString.Length <= 8 Then
                textRecord.data = tempValue
            Else
                addError(0, "Could not parse Data of Text Record", "Check Text Record Data Field")
                fatalError = True
            End If
        Catch ex As Exception
            addError(0, "Could not parse Data of Text Record", "Check Text Record Data Field")
            fatalError = True
        End Try

        Try
            tempValue = Convert.ToInt32(lineArray(4), 10)
            If lineArray(4).ToString.Length = 1 And tempValue < 5 Then
                textRecord.numberOfAdjustments = tempValue
            Else
                addError(0, "Could not parse Number of Adjustments of Text Record", "Check Text Record # of Adjustments Field")
                fatalError = True
            End If
        Catch ex As Exception
            addError(0, "Could not parse Number of Adjustments of Text Record", "Check Text Record # of Adjustments Field")
            fatalError = True
        End Try
        Dim x As Integer = 5
        While x < lineArray.Length - 1
            Dim adjustment As New Adjustment
            If lineArray(x).ToString.ToUpper = "A" Then
                adjustment.type = WindowsApplication1.Adjustment.Types.A
                adjustment.action = WindowsApplication1.Adjustment.Actions.None
                x = x + 1
            ElseIf lineArray(x).ToString.ToUpper = "R" Then
                adjustment.type = WindowsApplication1.Adjustment.Types.R

                If x + 2 < lineArray.Length - 1 Then
                    If lineArray(x + 1).ToString = "+" Then
                        adjustment.action = WindowsApplication1.Adjustment.Actions.Plus
                    ElseIf lineArray(x + 1).ToString = "-" Then
                        adjustment.action = WindowsApplication1.Adjustment.Actions.Minus
                    Else
                        adjustment.action = WindowsApplication1.Adjustment.Actions.None
                        addError(0, "Could not parse R type Sign in Adjustment of Text Record", "Check Text Record R type Adjustment Field")
                        fatalError = True
                    End If

                    Try
                        tempValue = Convert.ToInt32(lineArray(x + 2), 16)
                        If tempValue >= 0 Then
                            adjustment.loadPoint = tempValue
                        Else
                            addError(0, "Could not parse load point of Adjustment Record Type R of Text Record", "Check Adjustments Field")
                            fatalError = True
                        End If
                    Catch ex As Exception
                        addError(0, "Could not parse load point of Adjustment Record Type R of Text Record", "Check Adjustments Field")
                        fatalError = True
                    End Try

                Else
                    addError(0, "Could not parse R type Adjustment of Text Record", "Check Text Record R type Adjustment Field")
                    fatalError = True
                End If
                x = x + 3
            ElseIf lineArray(x).ToString.ToUpper = "E" Then
                adjustment.type = WindowsApplication1.Adjustment.Types.E

                If x + 2 < lineArray.Length - 1 Then
                    If lineArray(x + 1).ToString = "+" Then
                        adjustment.action = WindowsApplication1.Adjustment.Actions.Plus
                    ElseIf lineArray(x + 1).ToString = "-" Then
                        adjustment.action = WindowsApplication1.Adjustment.Actions.Minus
                    Else
                        adjustment.action = WindowsApplication1.Adjustment.Actions.None
                        addError(0, "Could not parse E type Sign in Adjustment of Text Record", "Check Text Record E type Adjustment Field")
                        fatalError = True
                    End If
                    adjustment.label = lineArray(x + 2)
                Else
                    addError(0, "Could not parse E type Adjustment of Text Record", "Check Text Record E type Adjustment Field")
                    fatalError = True
                End If
                x = x + 3

                'If x + 1 < lineArray.Length - 1 Then
                '    adjustment.label = lineArray(x + 1)
                'Else
                '    addError(0, "Could not parse E type Adjustment of Text Record", "Check Text Record E type Adjustment Field")
                'End If
                'x = x + 2
            Else
                addError(0, "Could not parse Adjustments of Text Record", "Check Text Record Adjustments Field")
            End If
            textRecord.adjustments.Add(adjustment)
        End While

        If textRecord.numberOfAdjustments <> textRecord.adjustments.Count Then
            addError(0, "Number of Adjustments in Text Record do not match actual number of adjustments", "Check Text Record Number of Adjustments")
        End If

        If lineArray(lineArray.Length - 1) = objectFile.programName Then
            textRecord.programName = lineArray(lineArray.Length - 1)
        Else
            addError(0, "Text Record Program Name does not match program name", "Check Text Record Program Name")
        End If

        If fatalError = False Then
            objectFile.textRecords.Add(textRecord)
        End If

    End Sub

    Private Sub parseLinkRecord(ByVal lineArray As Array, ByRef objectFile As ObjectFile, ByRef fatalError As Boolean)
        Dim linkingRecord As New LinkingRecord
        Dim tempValue As Integer

        If lineArray(1) <> "" And hasWhiteSpace(lineArray(1)) = False Then
            linkingRecord.name = lineArray(1)
        Else
            addError(0, "Linking Record Entry Name is Not Valid", "Check the Entry Name in the Link Record")
            fatalError = True
        End If

        Try
            tempValue = Convert.ToInt32(lineArray(2), 16)
            If lineArray(2).ToString.Length <= 4 And tempValue >= objectFile.programLoadPoint And tempValue <= objectFile.programLoadPoint + objectFile.programLength Then
                linkingRecord.address = tempValue
            Else
                addError(0, "Address of Linking Record Does not fall within the boundaries of the program", "Check Linking Record Address")
                fatalError = True
            End If
        Catch ex As Exception
            addError(0, "Could not parse Address of Linking Record", "Check Linking Record Address")
            fatalError = True
        End Try

        If lineArray(3).ToString.ToUpper = "START" Then
            linkingRecord.type = WindowsApplication1.LinkingRecord.LinkerTypes.Start
        ElseIf lineArray(3).ToString.ToUpper = "ENT" Then
            linkingRecord.type = WindowsApplication1.LinkingRecord.LinkerTypes.ENT
        Else
            addError(0, "Invalid Type Found in Linking Record", "Check Linking Record Type")
        End If

        If lineArray(4).ToString <> objectFile.programName Then
            addError(0, "Linking Record Does Not Have Same Program Name as Specified in Header Record", "Check Linking Record Program Name")
            fatalError = True
        Else
            linkingRecord.programName = lineArray(4).ToString
        End If

        If fatalError = False Then
            objectFile.linkingRecords.Add(linkingRecord)
        End If

    End Sub
    Private Function hasWhiteSpace(ByVal stringValue As String) As Boolean
        If stringValue.Contains(vbCrLf) Or stringValue.Contains(vbTab) Or stringValue.Contains(" ") Then
            Return True
        Else
            Return False
        End If
    End Function
    Private Sub parseHeader(ByVal lineArray As Array, ByRef objectFile As ObjectFile, ByRef fatalError As Boolean)
        Dim tempStringArray As Array
        Dim tempValue As Integer
        If lineArray(1) <> "" And hasWhiteSpace(lineArray(1)) = False Then
            objectFile.programName = lineArray(1)
        Else
            addError(0, "Could Not Find a valid Program Name in Header Record", "Check the Program Name")
            fatalError = True
        End If

        Try
            tempValue = Convert.ToInt32(lineArray(2), 16)
            If lineArray(2).ToString.Length = 4 And tempValue >= 0 Then
                objectFile.programLength = tempValue
            Else
                addError(0, "Could Not Parse Header Line Program Length", "Check Header Record")
                fatalError = True
            End If
        Catch ex As Exception
            addError(0, "Could Not Parse Header Line Program Length", "Check Header Record")
            fatalError = True
        End Try


        Try
            tempValue = Convert.ToInt32(lineArray(3), 16)
            If lineArray(3).ToString.Length <= 4 And tempValue >= 0 Then
                objectFile.programLoadPoint = tempValue
            Else
                addError(0, "Could Not Parse Header Line Program Load Address", "Check Header Record")
                fatalError = True
            End If
        Catch ex As Exception
            addError(0, "Could Not Parse Header Line Program Load Address", "Check Header Record")
            fatalError = True
        End Try

        If objectFile.programLoadPoint + objectFile.programLength > 65536 Then
            addError(0, "Program load point plus program length is larger than the current memory", "Check Header Record program length and load point")
            fatalError = True
        End If


        tempStringArray = Split(lineArray(4), ":")
        If tempStringArray.Length = 2 And tempStringArray(0).ToString.Length = 4 And tempStringArray(1).ToString.Length = 3 And IsNumeric(tempStringArray(0)) And IsNumeric(tempStringArray(1)) Then
            objectFile.dateValue = lineArray(4)
        Else
            addError(0, "Could Not Parse Header Line Program Date", "Check Date Format so that it matches YYYY:DDD")
        End If

        tempStringArray = Split(lineArray(5), ":")
        If tempStringArray.Length = 3 And tempStringArray(0).ToString.Length = 2 And tempStringArray(1).ToString.Length = 2 And tempStringArray(2).ToString.Length = 2 And IsNumeric(tempStringArray(0)) And IsNumeric(tempStringArray(1)) And IsNumeric(tempStringArray(2)) Then
            objectFile.dateValue = lineArray(5)
        Else
            addError(0, "Could Not Parse Header Line Program Time", "Check Date Format so that it matches HH:MM:SS")
        End If


        Try
            tempValue = Convert.ToInt32(lineArray(6), 16)
            If lineArray(6).ToString.Length <= 4 And tempValue >= 0 Then
                objectFile.numberOfLinkingRecords = tempValue
            Else
                addError(0, "Could Not Parse Header Line Number Of Linking Records", "Check the Number of Linking Records")
            End If
        Catch ex As Exception
            addError(0, "Could Not Parse Header Line Number Of Linking Records", "Check the Number of Linking Records")
        End Try

        Try
            tempValue = Convert.ToInt32(lineArray(7), 16)
            If lineArray(7).ToString.Length <= 4 And tempValue >= 0 Then
                objectFile.numberOfTextRecords = tempValue
            Else
                addError(0, "Could Not Parse Header Line Number Of Text Records", "Check the Number of Text Records")
            End If
        Catch ex As Exception
            addError(0, "Could Not Parse Header Line Number Of Text Records", "Check the Number of Text Records")
        End Try

        Try
            tempValue = Convert.ToInt32(lineArray(8), 16)
            'MsgBox(tempValue)
            'MsgBox(objectFile.programLoadPoint & vbCrLf & objectFile.programLoadPoint + objectFile.programLength & vbCrLf & objectFile.programLength)
            If lineArray(8).ToString.Length <= 4 And tempValue >= objectFile.programLoadPoint And tempValue <= (objectFile.programLoadPoint + objectFile.programLength) Then
                objectFile.executionStartAddress = tempValue
            Else
                addError(0, "Could Not Parse Header Line Execution Start Address, may be out of bounds.", "Check the Execution Start Address")
            End If
        Catch ex As Exception
            addError(0, "Could Not Parse Header Line Execution Start Address, may be out of bounds.", "Check the Execution Start Address")
            fatalError = True
        End Try


        If lineArray(9) = "SAL" Then
            objectFile.executionStartAddress = tempValue
        Else
            addError(0, "Could not identify the Programing Language in the Header Record", "Make sure you are using the right linker for the SAL560")
        End If

        If lineArray(10) <> "" Then
            objectFile.version = lineArray(10)
        Else
            addError(0, "No Version Found in Header Record", "Add a Version")
        End If

        If lineArray(11) <> "" Then
            objectFile.revision = lineArray(11)
        Else
            addError(0, "No Revision Found in Header Record", "Add a Revision")
        End If

        If lineArray(12) <> objectFile.programName Then
            addError(0, "Program Names do not match.", "Check Program Names so that they match")
        End If

    End Sub

    Private Sub addError(ByVal errorNumber As Integer, ByVal errorString As String, ByVal errorCorrection As String)
        Dim errorItem As New ListViewItem(errorNumber)
        errorItem.SubItems.Add(errorString)
        errorItem.SubItems.Add(errorCorrection)
        'MsgBox(currentLine)
        If currentLine <> 0 Then
            errorItem.SubItems.Add(currentObjectFile)
            errorItem.SubItems.Add(currentLine)
        End If

        ListView1.Items.Add(errorItem)
        ListView1.AutoResizeColumns(ColumnHeaderAutoResizeStyle.ColumnContent)
    End Sub

    Private Sub ListView2_MouseUp(ByVal sender As Object, ByVal e As System.Windows.Forms.MouseEventArgs) Handles ListView2.MouseUp
        Dim newItem As ListViewItem = ListView2.GetItemAt(e.X, e.Y)
        If e.Button = Windows.Forms.MouseButtons.Right Then
            If newItem Is Nothing = False Then
                For Each x As Object In filesToBeLinked
                    If x.ToString = newItem.Text Then
                        filesToBeLinked.Remove(x)
                        Exit For
                    End If
                Next

                ListView2.Items.Remove(newItem)
            End If
        End If

    End Sub


    Private Sub ListView2_SelectedIndexChanged(ByVal sender As System.Object, ByVal e As System.EventArgs) Handles ListView2.SelectedIndexChanged

    End Sub
End Class

Public Class ObjectFile
    Public programName As String
    Public programLength As Integer
    Public programLoadPoint As Integer
    Public numberOfLinkingRecords As Integer
    Public numberOfTextRecords As Integer
    Public executionStartAddress As Integer
    Public version As String
    Public revision As String
    Public linkingRecords As New ArrayList
    Public dateValue As String
    Public timeValue As String
    Public textRecords As New ArrayList
End Class

Public Class LinkingRecord
    Public name As String
    Public address As Integer
    Public type As LinkerTypes
    Public programName As String

    Public Enum LinkerTypes
        Start = 0
        ENT = 1
    End Enum
End Class

Public Class TextRecord
    Public address As Integer
    Public debug As Boolean
    Public data As Integer
    Public numberOfAdjustments As Integer
    Public programName As String
    Public adjustments As New ArrayList

End Class

Public Class Adjustment
    Public type As Types
    Public action As Actions
    Public label As String
    Public labelValue As Integer
    Public loadPoint As String
    Public Enum Actions
        Plus = 0
        Minus = 1
        None = 2
    End Enum
    Public Enum Types
        R = 0
        A = 1
        E = 2
    End Enum
End Class
