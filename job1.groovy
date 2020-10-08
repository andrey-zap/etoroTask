node('master')
{
  try {
    stage('clean workspace & checkout source SCM')
  {
      cleanWs()
      checkout scm
  }
    stage('next job')
  {
      bat 'dir'
      retry(3) {
        try {
          build quietPeriod: 0, job: 'SecondJob', parameters: [[$class: 'StringParameterValue', name: 'parentJobName', value: "${JOB_NAME}" ],
      [$class: 'StringParameterValue', name: 'parentJobBuildNum', value: "${BUILD_NUMBER}" ]]
         }catch (Exception e)
         {
          error 'child JOB Failed!'
         }
      }
  }
  }catch (Exception  err)
{
    println "error on ${JOB_NAME}, error: " + err.getMessage()
    currentBuild.result = 'FAILURE'
}
}
