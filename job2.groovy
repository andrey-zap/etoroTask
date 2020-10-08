node('master')
{
  properties([parameters([string(defaultValue: 'b', description: 'c', name: 'parentJobName', trim: true),
   string(defaultValue: 'bb', description: 'cc', name: 'parentJobBuildNum', trim: true)])])

  try {
    stage('run script')
  {
    /*
    this stage responsible for running the desired script (success only on 3, 6, 9... BUILD_NUMBER % 3 == 0)
    */
      dir("..//${parentJobName}")
    {
        bat 'etoro-script.bat %JOB_NAME% %BUILD_NUMBER%'
    }
  }

    stage('trigger last job')
  {
    /*
    , if prev stage succeeded, triggering the next job and passing rootJobBuildNum argument
    */
      try {
        build quietPeriod: 0, job: 'ThirdJob',  parameters: [[$class: 'StringParameterValue', name: 'rootJobBuildNum', value: "${parentJobBuildNum}"]]
    }catch (Exception e)
    {
        error 'error on Child job!'
    }
  }
}catch (Exception  err)
{
    println "error on ${JOB_NAME}, error: " + err.getMessage()
    currentBuild.result = 'FAILURE'
}
}
