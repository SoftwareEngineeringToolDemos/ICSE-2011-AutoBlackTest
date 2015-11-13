## VAGRANT Instructions

### Configuration details :
  * VM OS : _Windows 7_
  * Base Box : _modernIE/w7-ie10_
  *	Check for updates : _False_
  * Number of CPUs : _2_
  * System Memory : _2048_
  *	Name : Perquimans-sshekha3-AutoBlackTest

### Instructions

1. Download and install [Vagrant](https://www.vagrantup.com/downloads.html).
2. Download and install [VirtualBox](https://www.virtualbox.org/wiki/Downloads).
3. Clone the project repository for accessing files: 'git clone https://github.com/SoftwareEngineeringToolDemos/ICSE-2011-AutoBlackTest.git'
4. Once project is cloned, you can go to build-vm folder and start running Vagrant: 'vagrant up' ( --provision flag is needed to redo provisions)
  * This would install the necessary softwares for the tool. (JRE 1.7 is installed using Chocolatey)


### Acknowledgements

Used base box from [Atlas Hashicorp - ModernIE](https://atlas.hashicorp.com/modernIE/boxes/w7-ie10).
Used [Chocolatey Non-administrative install powershell script](https://github.com/chocolatey/choco/wiki/Installation#command-line) to install Chocolatey.
Used Chocolatey to install Java. 

### Notes
  * The software pre-requisite is Java 1.7 and it could be verified by going to command prompt and entering 'java -version'
  *	The Base box VM license expires on [01-21-2016](https://atlas.hashicorp.com/modernIE/boxes/w7-ie10).