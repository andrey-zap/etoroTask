node('master')
{
  properties([parameters([string(defaultValue: 'none', description: 'root parent build number', name: 'rootJobBuildNum', trim: true)])])

  try {
    stage('print root parent build num')
  {
      println "This is Third Job! Build num of root parent: \"${rootJobBuildNum}\""
  }
 }catch (Exception  err)
{
    println "error on ${JOB_NAME}, error: " + err.getMessage()
    currentBuild.result = 'FAILURE'
}
}
