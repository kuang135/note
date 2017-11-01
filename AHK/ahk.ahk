;-----------------------------------------------------------------------------------------------------------
;【屏幕底部右键剪切板历史】
;▼
#If MouseIsBottomR()
RButton::
CoordMode,Mouse
MouseGetPos,MouseX,MouseY
MouseXSaved := MouseX
ClipSaved1Short := SubStr(ClipSaved1,1,40)  ;使用:=赋值防止AutoTrim在Var1=%Var2%这样的语句中进行赋值时忽略Var2的前导和尾随空格和tab以及换行
ClipSaved2Short := SubStr(ClipSaved2,1,40)
ClipSaved3Short := SubStr(ClipSaved3,1,40)
Gui,ClipboardX:New
Gui,Margin,0,0
Gui,Font,s10
Gui,+AlwaysOnTop -SysMenu -Caption +Owner
Gui,Add,ListView,r4 Grid -Hdr AltSubmit gCliphistory,Cliphistory    ;使用AltSubmit使listview响应更多g标签
LV_Add("",ClipSaved1Short)
LV_Add("",ClipSaved2Short)
LV_Add("",ClipSaved3Short)
LV_Add("","取消")
Gui,Show,x%MouseX% y678 NoActivate  
Hotkey,LButton,WaitClick,On     ;使用监听左键而非响应g标签单击事件来防止单击时目标窗口失去焦点
return
#If

WaitClick:
Hotkey,LButton,Off
CoordMode,Mouse
MouseGetPos,MouseX,MouseY
If (MouseX > (MouseXSaved - 5)) && (MouseX < (MouseXSaved + 376)) && (MouseY > 678)
{
    If (MouseY > 678)
    {
        If (MouseY < 699)
        SelectItem := 1
        Else If (MouseY < 721)
        SelectItem := 2
        Else If (MouseY < 742)
        SelectItem := 3
        Else
        SelectItem := 4
        Gui,ClipboardX:Hide
        ClipSaved1Saved := ClipSaved1
        ClipSaved2Saved := ClipSaved2
        ClipSaved3Saved := ClipSaved3
        ClipSaved := Clipboard
        Clipboard := ClipSaved%SelectItem%
        Sleep,100               ;脚本自身改变剪贴板的内容OnClipboardChange标签通常不会立即执行,即在它之前很可能还会执行改变剪贴板内容的命令之后的命令.要强制立即执行这个标签,在改变剪贴板后使用短暂的延迟
        Send ^v                 ;Send %Clipboard%有输入延迟，只有粘贴操作可以瞬发
        Clipboard := ClipSaved
        Sleep,100
        ClipSaved1 := ClipSaved1Saved
        ClipSaved2 := ClipSaved2Saved
        ClipSaved3 := ClipSaved3Saved
    }
}
Else
{
    Gui,ClipboardX:Hide
}
return

Cliphistory:
If (A_GuiEvent = "Normal")
{
    Gui,Hide
    If (A_EventInfo <> 4)
    {
            ClipSaved1Saved := ClipSaved1
        ClipSaved2Saved := ClipSaved2
        ClipSaved3Saved := ClipSaved3
        ClipSaved := Clipboard
        Clipboard := ClipSaved%A_EventInfo%
        Sleep,100
        Send ^v
        Clipboard := ClipSaved
        Sleep,100
        ClipSaved1 := ClipSaved1Saved
        ClipSaved2 := ClipSaved2Saved
        ClipSaved3 := ClipSaved3Saved
    }
}
return

OnClipboardChange:
If (A_EventInfo < 2)
{
    ClipSaved3 := ClipSaved2
    ClipSaved2 := ClipSaved1
    ClipSaved1 := Clipboard
}
return
MouseIsBottomR()
{
    CoordMode,Mouse
    MouseGetPos,MouseX,MouseY
    return MouseY>A_ScreenHeight-5 && MouseX>20 && MouseX<A_ScreenWidth-20
}
;▲
-----------------------------------------------------------------------------------------------------------
;【屏幕右侧右键显示任务栏】
;▼
#If MouseIsRIghtR()
RButton::
WinSet,AlwaysOnTop,On,ahk_class Shell_TrayWnd
WinActivate,ahk_class Shell_TrayWnd
return
#If
MouseIsRightR()
{
    CoordMode,Mouse
    MouseGetPos,MouseX,MouseY
    return MouseX>A_ScreenWidth-5 && MouseY>20 && MouseY<A_ScreenHeight-20
}
;▲
-----------------------------------------------------------------------------------------------------------
;【屏幕左下角中键命令提示符】
;▼
#If MouseIsLeftBottomM()
MButton::
^Capslock::
Run, RunAsW.exe "C:\Windows\System32\cmd.exe" "C:\Users\gukai"      ;RunAsW第二个参数设定启动目录
return
#If
MouseIsLeftBottomM()
{
    CoordMode,Mouse
    MouseGetPos,MouseX,MouseY
    return MouseX<5 && MouseY>A_ScreenHeight-5
}
;▲
-----------------------------------------------------------------------------------------------------------
;【窗口标题中键点击置顶】
;▼
#If MouseIsRTop50M()
MButton::
^Capslock::
WinGetClass,Class,A
If (Class = "CabinetWClass")    ;Clover需要检测到的class转化为Clover_WidgetWin_0进行相关操作
Class = Clover_WidgetWin_0
WinSet,AlwaysOnTop,Toggle,ahk_Class %Class%
Gui,New
Gui,Color,0xFFFFFF
Gui,Font,s50,黑体
Gui,Add,Text,cBlue,置顶
Gui,+AlwaysOnTop -SysMenu -Caption +Owner
Gui,+LastFound
WinSet,TransColor,0xFFFFFF
Gui,Show,NoActivate
Sleep,300
Gui,Hide
return
#If
MouseIsRTop50M()
{
    CoordMode,Mouse,Relative
    MouseGetPos,MouseX,MouseY
    return MouseY<50
}
;▲
-----------------------------------------------------------------------------------------------------------
;【屏幕左上角滚轮放置窗口】
;▼
#If MouseIsLeftTopW()
WheelUp::
WinGetPos,,,TaskbarWidth,,ahk_class Shell_TrayWnd
Width := A_ScreenWidth - TaskbarWidth
WinMove,A,,0,0,%Width%,%A_ScreenHeight%
WinSet,AlwaysOnTop,Off,A
return

WheelDown::
X := A_ScreenWidth / 6
Y := A_ScreenHeight / 6
Width := (A_ScreenWidth / 3) * 2
Height := (A_ScreenHeight / 3) * 2
WinMove,A,,%X%,%Y%,%Width%,%Height%
#If
MouseIsLeftTopW()
{
    CoordMode,Mouse
    MouseGetPos,MouseX,MouseY
    return MouseX<5 && MouseY<5
}
;▲
-----------------------------------------------------------------------------------------------------------
;【屏幕左侧滚轮快速翻页】
;▼
#If MouseIsLeftW()
WheelUp::Send {PgUp}
WheelDown::Send {PgDn}
#If
MouseIsLeftW()
{
    CoordMode,Mouse
    MouseGetPos,MouseX,MouseY
    return MouseX<5 && MouseY>20 && MouseY<A_ScreenHeight-20
}
;▲
-----------------------------------------------------------------------------------------------------------
;【屏幕左下角滚轮最小化/恢复】
;▼
#If MouseIsLeftBottomW()
WheelUp::
WinRestore,%Title%
WinActivate,%Title%
return
WheelDown::
WinGetTitle,Title,A
WinGetClass,Class,A
WinMinimize,A
If (Class = "CabinetWClass")
{
    WinGet,ID,id,ahk_class Clover_WidgetWin_0
    Title := "ahk_id" + ID
}
return
#If
MouseIsLeftBottomW()
{
    CoordMode,Mouse
    MouseGetPos,MouseX,MouseY
    return MouseX<5 && MouseY>A_ScreenHeight-5
}
;▲
-----------------------------------------------------------------------------------------------------------
;【屏幕底部滚轮放置窗口】
;▼
#If MouseIsBottomW()
WheelUp::
WinGetPos,,,TaskbarWidth,,ahk_class Shell_TrayWnd
Width := A_ScreenWidth - TaskbarWidth
Height := A_ScreenHeight / 2
WinMove,A,,0,0,%Width%,%Height%
return
WheelDown::
IfWinActive,- Word
{
    WinRestore,A        ;最大化状态下排列Word无法正常缩放
    WinGetPos,,,TaskbarWidth,,ahk_class Shell_TrayWnd
    Width := A_ScreenWidth - TaskbarWidth
    Height := A_ScreenHeight - 520
    WinMove,A,,0,520,%Width%,%Height%
}
Else
{
    WinGetPos,,,TaskbarWidth,,ahk_class Shell_TrayWnd
    Width := A_ScreenWidth - TaskbarWidth
    Height := A_ScreenHeight / 2
    WinMove,A,,0,%Height%,%Width%,%Height%
}
WinSet,AlwaysOnTop,Toggle,A
return
#If
MouseIsBottomW()
{
    CoordMode,Mouse
    MouseGetPos,MouseX,MouseY
    return MouseY>A_ScreenHeight-5 && MouseX>5 && MouseX<A_ScreenWidth-5
}
;▲
-----------------------------------------------------------------------------------------------------------
;【任务栏上滚轮调整音量】
;▼
#If MouseIsOverTaskBarW()
WheelUp::Send {Volume_Up}
WheelDown::Send {Volume_Down}
#If
MouseIsOverTaskBarW()
{   
    CoordMode,Mouse
    MouseGetPos,,,ID
    WinGetClass,Class,ahk_id %ID%
    return Class = "Shell_TrayWnd"
}
;▲
-----------------------------------------------------------------------------------------------------------
;【屏幕顶部滚轮放置窗口】
;▼
#If MouseIsTopW()
WheelUp::
WinGetClass,Class,A
If (Class = "WorkerW") || (Class = "Shell_TrayWnd")             ;桌面的顶部滚轮切换Fences页面
{
    Send !{Left}
}
Else
{
    WinGetPos,,,TaskbarWidth,,ahk_class Shell_TrayWnd
    Width := (A_ScreenWidth - TaskbarWidth)/2
    Height := A_ScreenHeight
    WinMove,A,,0,0,%Width%,%Height%
}
return
WheelDown::
WinGetClass,Class,A
If (Class = "WorkerW") || (Class = "Shell_TrayWnd")
{
    Send !{Right}
}
Else
{
    WinGetPos,,,TaskbarWidth,,ahk_class Shell_TrayWnd
    Width := (A_ScreenWidth - TaskbarWidth)/2
    Height := A_ScreenHeight
    WinMove,A,,%Width%,0,%Width%,%Height%
}
return
#If
MouseIsTopW()
{
    CoordMode,Mouse
    MouseGetPos,MouseX,MouseY
    return MouseY<5
}
;▲
-----------------------------------------------------------------------------------------------------------
;【Ctrl+Win+X转换为大写】
;▼
^#x::
Send ^c
StringUpper,Clipboard,Clipboard
Send ^v
return
;▲
-----------------------------------------------------------------------------------------------------------
;【按住空格键快捷输入】
;▼
Space::Space                ;单独按下时保持原有功能

Space & a::Send 5
Space & s::Send 2
Space & d::Send 3
Space & f::Send 4

Space & i::Send {Blind}{Up}     ;使用重映射保持连续选择功能
Space & k::Send {Blind}{Down}
Space & j::Send {Blind}{Left}
Space & l::Send {Blind}{Right}

Space & n::Send {PgUp}
Space & m::Send {PgDn}
Space & u::Send {Blind}{Home}
Space & o::Send {Blind}{End}
Space & q::Send ^{Home}
Space & e::Send ^{End}
Space & VKBC::Send ^{PgUp}
Space & VKBE::Send ^{PgDn}

Space & x::Send ^x
Space & c::Send ^c
Space & v::Send ^v
Space & z::Send ^z
Space & y::Send ^y

Space & 2::Send {F2}

Space & Tab::Send {Tab}

Space & VKBF::Send {Delete}
Space & VKBA::Send {BackSpace}  ;分号属于特殊字符，不在按键列表中，可以通过查找键码将其设为热键

Space & w::Goto,ALTF4
;▲
-----------------------------------------------------------------------------------------------------------
;【缩写+Enter运行程序】
;▼
#Hotstring EndChars `n

::rjsb::
Run, notepad.exe
return

;▲
-----------------------------------------------------------------------------------------------------------
;【百度输入法】
;▼
#IfWinExist,inputBar

;【Capslock键翻页，长按上一页】
Capslock::
KeyWait,Capslock,T0.2
If (Errorlevel = 0)
{
    Send {PgDn}
}
Else
{
    Send {PgUp}
    KeyWait,Capslock
}
return

;【Space&Capslock翻页】
Space & Capslock::Send {PgDn}
