cd %USERPROFILE%\AppData\Roaming
mkdir Buddi
cd Buddi
mkdir Languages
xcopy C:\workspaceDist\ca.digitalcave.buddi\etc\Languages Languages
move /Y C:\ClonedFiles\build-vm\shellScripts\start_eclipse.bat "C:\Users\IEUser\AppData\Roaming\Microsoft\Windows\Start Menu\Programs\Startup\"