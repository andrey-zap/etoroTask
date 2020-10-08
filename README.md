# eToroTask
There are many ways to install jenkins (in docker compose, or with a web server and WAR or with helm charts) but since we are on windows server 2016 I decided to keep it simple as possible and used CHOCOLATE package manager for windows with powershell(next is the installation process):

1) check if execution policy allows me to install Chocolate (similar to apt-get in Debian), if its "Restricted" then we wont be able to run scripts on the server
- Get-ExecutionPolicy

2) setting the execution policy to Bypass mode &  downloading and installing chocolate
- Set-ExecutionPolicy Bypass -Scope Process -Force; [System.Net.ServicePointManager]::SecurityProtocol = [System.Net.ServicePointManager]::SecurityProtocol -bor 3072; iex ((New-Object System.Net.WebClient).DownloadString('https://chocolatey.org/install.ps1'))

3)checking version of the installed module 
- choco -?

4) installing jenkins as a service! (easiest way, no need to mess with Web server configs, no need to install Java)
- cinst -Y jenkins   

5) modifying  the file C:\Program Files (x86)\Jenkins\config.xml, to set default port on 80 (instead of 8080) as requested in the task. modify the <arguments> section , setting --httpPort=80
 and restarting the service to apply the changes
- Restart-Service jenkins
6) open browser and go to http://localhost




# some points:
- As said, Jenkins installed using chocolate package manager, as requeted in step 1:
	- a. it's  accessible  from http://localhost (80)
	- b. Jenkins installation includes only 1 master server without slaves nodes so to perform multiple parallel job run we need to set bigger number of executors on the master (set to 10). (in prod env we should use slaves ofcorse)
- since we are on windows server, for simplicity I wrote the script using BATCH/cmd, script name is etoro-script.bat:
	- the script accepts  2 arguments, job name and build number
	- to fail only on every 3 builds I used modulu function BUILD_NUM % 3 == 0  (this ensures that only 3,6,9,12... builds are success)
- the requested jobs are called FirstJob, SecondJob, ThirdJob
- all the jobs are written in groovy scripted pipeline style and all of them are fetched from github (pipeline from SCM) for backup and single source of truth

# first job:
1) cleaning workspace and checkout SCM github repo to get the script
2) calling the second job with retry block of 3 times total (as requested in the task)

# Second job:
- for simplicity, this job requires 2 parameters used in the flow (root_job_name, root_job_build_num)
- this job triggers the script using the relative path to the first job workspace (not 100% generic but tried to keep it simple, another approach is to use shared libraries or maybe some shared workspace plugins)
- if build number of the job divided  by 3 then the script will succeed and trigger the last third job

# Third Job:
	- requires 1 parameter,  root_job_build_num
	- prints the root parent job number 
	
if one of the jobs fails, all the flow fails!