## Vagrant Instructions

### Base Box Login details :
  *	User : **IEUser**
  *	Password : **Passw0rd!**

### Configuration details :
  * VM OS : **Windows 7**
  * Base Box : **modernIE/w7-ie10**
  *	Check for updates : **False**
  * Number of CPUs : **2**
  * System Memory : **2048**
  *	Name : **Perquimans-sshekha3-AutoBlackTest**


### Instructions : 

1. Download and install [Vagrant](https://www.vagrantup.com/downloads.html).
2. Download and install [VirtualBox](https://www.virtualbox.org/wiki/Downloads).
3. Clone the project repository for accessing files: 

 ``git clone https://github.com/SoftwareEngineeringToolDemos/ICSE-2011-AutoBlackTest.git``
4. Download [IBM Rational Functional Tester Trial](http://www.ibm.com/developerworks/downloads/r/rft/index.html), it would require you to sign up, provide some details and then give you the link to download the setup files. The script works with version 8.6.0.3.
5. Move the downloaded folders : ``RFT_8.6.0.3_EVL_CORE`` ``RFT_8.6.0.3_EVL_Setup`` to ``build-vm\syncedFolder\``. This is the folder which is used in VagrantFile as Synced folder.
6. Now you can go to build-vm folder and start running Vagrant: `vagrant up` ( --provision flag is needed to redo provisions)
  * This would install the necessary softwares for the tool including:
  * Chocolatey (a Machine Package Manager)
  * JRE 1.7_79 is installed using Chocolatey
  * Git is installed using Chocolatey
  * IBM Rational Functional Tester trial is installed using the synced folder copies and a ResponseFile.xml which is used for silent install
7. Notes :
  * IBM Rational Functional Tester trial is shipped with a default JVM which is not compatible with our tool, hence some changes are made by the vagrant script to reconfigure defaults of RFT and enable a new JRE for testing.
  * Finally, necessary files on the desktop are created.


### Acknowledgements : 

 * Used base box from [Atlas Hashicorp - ModernIE](https://atlas.hashicorp.com/modernIE/boxes/w7-ie10).
 * Used [Chocolatey Non-administrative install powershell script](https://github.com/chocolatey/choco/wiki/Installation#command-line) to install Chocolatey.
 * Used [IBM Rational Functional Tester Trial](http://www.ibm.com/developerworks/downloads/r/rft/index.html) version for the tool (mandatory requirement).
 * Used Chocolatey to install Java. 

### Notes
  * The software pre-requisite is Java 1.7 and it could be verified by going to command prompt and entering `java -version`
  *	The Base box VM license expires on [01-21-2016](https://atlas.hashicorp.com/modernIE/boxes/w7-ie10).
