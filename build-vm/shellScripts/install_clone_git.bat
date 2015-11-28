%ALLUSERSPROFILE%\chocolatey\bin\choco install git -y
cd C:\Program Files\Git\bin\
git clone https://github.com/SoftwareEngineeringToolDemos/ICSE-2011-AutoBlackTest.git C:\ClonedFiles\
cd C:\ClonedFiles\
move AutoBlackTest C:\workspaceDist
move vm-uploads C:\vm_uploads
cd vm-desktop
move /Y * C:\Users\IEUser\Desktop
move /Y Licences C:\Users\IEUser\Desktop\
cd C:\Users\IEUser\Desktop
ren Licences Licenses
xcopy C:\ClonedFiles\build-vm\shellScripts\start_eclipse.bat C:\Users\IEUser\Desktop
ren start_eclipse.bat App-Starter.bat
