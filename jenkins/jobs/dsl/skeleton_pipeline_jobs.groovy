//job('DSL-Tutorial-1-Test') {
    // scm {
    //     git('git://github.com/quidryan/aws-sdk-test.git')
    // }
    // triggers {
    //     scm('H/15 * * * *')
    // }
    // steps {
    //     maven('-e clean test')
    // }
//}
//import pluggable.scm.*;
//import adop.cartridge.properties.*;

//SCMProvider scmProvider = SCMProviderHandler.getScmProvider("${SCM_PROVIDER_ID}", binding.variables)
//CartridgeProperties cartridgeProperties = new CartridgeProperties("${CARTRIDGE_CUSTOM_PROPERTIES}");

// // Folders
def workspaceFolderName = "${WORKSPACE_NAME}"
def projectFolderName = "${PROJECT_NAME}"
def projectScmNamespace = "${SCM_NAMESPACE}"

// // Variables
// // **The git repo variables can be changed to the users' git repositories when loading the cartridge and populating the CARTRIDGE_CUSTOM_PROPERTIES with "scm.code.repo.name" and "scm.test.repo.name" properties.
// def skeletonAppgitRepo = cartridgeProperties.getProperty("scm.code.repo.name", "YOUR_APPLICATION_REPO");
// def regressionTestGitRepo = cartridgeProperties.getProperty("scm.test.repo.name", "YOUR_REGRESSION_TEST_REPO");

// // ** The logrotator variables should be changed to meet your build archive requirements
// def logRotatorDaysToKeep = 2
// def logRotatorBuildNumToKeep = 2
// def logRotatorArtifactsNumDaysToKeep = 2
// def logRotatorArtifactsNumToKeep = 2

// // Jobs
def buildAppJob = freeStyleJob(projectFolderName + "/Build_Job")
def unitTestJob = freeStyleJob(projectFolderName + "/Testing_Job")
//def codeAnalysisJob = freeStyleJob(projectFolderName + "/Skeleton_Application_Code_Analysis")
//def deployJob = freeStyleJob(projectFolderName + "/Skeleton_Application_Deploy")
//def regressionTestJob = freeStyleJob(projectFolderName + "/Skeleton_Application_Regression_Tests")

// Views
def pipelineView = buildPipelineView(projectFolderName + "/Viewing")

// pipelineView.with{
//     title('Skeleton Application Pipeline')
//     displayedBuilds(5)
//     selectedJob(projectFolderName + "/Skeleton_Application_Build")
//     showPipelineParameters()
//     showPipelineDefinitionHeader()
//     refreshFrequency(5)
// }

// All jobs are tied to build on the Jenkins slave
// The functional build steps for each job have been left empty
// A default set of wrappers have been used for each job
// New jobs can be introduced into the pipeline as required

//MY NEW SCM
// scm {
//   git 
// }

buildAppJob.with{
  //description("Skeleton application build job.")
  //scm scmProvider.get(projectScmNamespace, skeletonAppgitRepo, "*/master", "adop-jenkins-master", null)
  //environmentVariables {
      //env('WORKSPACE_NAME',workspaceFolderName)
     // env('PROJECT_NAME',projectFolderName)
  //}
  label("docker")
  steps {
    shell('''echo "I\'m building"'''.stripMargin())
    scm {
      git {
        remote {
          branch('master')
          url('https://github.com/t3resa/maven-hello-world.git')
        }
      }
    }
    //maven ('clean verify', 'my-app/pom.xml')
    maven {
      mavenInstallation('ADOP Maven')
      rootPOM('my-app/pom.xml')
      goals('clean')
      goals('verify')
      goals('install')
    }
    //shell('''jar -t my-app/target/my-app-1.0-SNAPSHOT.jar''')
  }
  publishers{
    downstream(projectFolderName + "/Testing_Job")
    // downstreamParameterized{
    //   trigger(projectFolderName + "/Testing_Job"){
    //     condition("UNSTABLE_OR_BETTER")
    //     parameters{
    //       predefinedProp("B",'${BUILD_NUMBER}')
    //       predefinedProp("PARENT_BUILD", '${JOB_NAME}')
    //     }
    //   }
    //  }
   }
}

unitTestJob.with{
//   description("This job runs unit tests on our skeleton application.")
  // parameters{
  //   stringParam("B",'',"Parent build number")
  //   stringParam("PARENT_BUILD","Skeleton_Application_Build","Parent build name")
  // }
  // logRotator {
  //   daysToKeep(logRotatorDaysToKeep)
  //   numToKeep(logRotatorBuildNumToKeep)
  //   artifactDaysToKeep(logRotatorArtifactsNumDaysToKeep)
  //   artifactNumToKeep(logRotatorArtifactsNumToKeep)
  // }
  // wrappers {
  //   preBuildCleanup()
  //   injectPasswords()
  //   maskPasswords()
  //   sshAgent("adop-jenkins-master")
  // }
  // environmentVariables {
  //     env('WORKSPACE_NAME',workspaceFolderName)
  //     env('PROJECT_NAME',projectFolderName)
  // }
   label("docker")
//   steps {
//   }
   steps {
     shell('''echo "I\'m Testing"'''.stripMargin())
   }
//   publishers{
//     downstreamParameterized{
//       trigger(projectFolderName + "/Skeleton_Application_Code_Analysis"){
//         condition("UNSTABLE_OR_BETTER")
//         parameters{
//           predefinedProp("B",'${B}')
//           predefinedProp("PARENT_BUILD",'${PARENT_BUILD}')
//         }
//       }
//     }
//   }
}

// node {
//  should keep commented out: stage('SCM') {
//     git 'https://github.com/foo/bar.git'
//   }
//   stage('SonarQube analysis') {
//     // requires SonarQube Scanner 2.8+
//     def scannerHome = tool 'SonarQube Scanner 2.8';
//     withSonarQubeEnv('My SonarQube Server') {
//       sh "${scannerHome}/bin/sonar-scanner"
//     }
//   }
// }

// codeAnalysisJob.with{
//   description("This job runs code quality analysis for our skeleton application using SonarQube.")
//   parameters{
//     stringParam("B",'',"Parent build number")
//     stringParam("PARENT_BUILD","Skeleton_Application_Build","Parent build name")
//   }
//   logRotator {
//     daysToKeep(logRotatorDaysToKeep)
//     numToKeep(logRotatorBuildNumToKeep)
//     artifactDaysToKeep(logRotatorArtifactsNumDaysToKeep)
//     artifactNumToKeep(logRotatorArtifactsNumToKeep)
//   }
//   environmentVariables {
//       env('WORKSPACE_NAME',workspaceFolderName)
//       env('PROJECT_NAME',projectFolderName)
//   }
//   wrappers {
//     preBuildCleanup()
//     injectPasswords()
//     maskPasswords()
//     sshAgent("adop-jenkins-master")
//   }
//   label("docker")
//   steps {
//     shell('''## YOUR CODE ANALYSIS STEPS GO HERE'''.stripMargin())
//   }
//   publishers{
//     downstreamParameterized{
//       trigger(projectFolderName + "/Skeleton_Application_Deploy"){
//         condition("UNSTABLE_OR_BETTER")
//         parameters{
//           predefinedProp("B",'${B}')
//           predefinedProp("PARENT_BUILD", '${PARENT_BUILD}')
//         }
//       }
//     }
//   }
// }

// deployJob.with{
//   description("This job deploys the skeleton application to the CI environment")
//   parameters{
//     stringParam("B",'',"Parent build number")
//     stringParam("PARENT_BUILD","Skeleton_Application_Build","Parent build name")
//     stringParam("ENVIRONMENT_NAME","CI","Name of the environment.")
//   }
//   logRotator {
//     daysToKeep(logRotatorDaysToKeep)
//     numToKeep(logRotatorBuildNumToKeep)
//     artifactDaysToKeep(logRotatorArtifactsNumDaysToKeep)
//     artifactNumToKeep(logRotatorArtifactsNumToKeep)
//   }
//   wrappers {
//     preBuildCleanup()
//     injectPasswords()
//     maskPasswords()
//     sshAgent("adop-jenkins-master")
//   }
//   environmentVariables {
//       env('WORKSPACE_NAME',workspaceFolderName)
//       env('PROJECT_NAME',projectFolderName)
//   }
//   label("docker")
//   steps {
//     shell('''## YOUR DEPLOY STEPS GO HERE'''.stripMargin())
//   }
//   publishers{
//     downstreamParameterized{
//       trigger(projectFolderName + "/Skeleton_Application_Regression_Tests"){
//         condition("UNSTABLE_OR_BETTER")
//         parameters{
//           predefinedProp("B",'${B}')
//           predefinedProp("PARENT_BUILD", '${PARENT_BUILD}')
//           predefinedProp("ENVIRONMENT_NAME", '${ENVIRONMENT_NAME}')
//         }
//       }
//     }
//   }
// }

// regressionTestJob.with{
//   description("This job runs regression tests on the deployed skeleton application")
//   parameters{
//     stringParam("B",'',"Parent build number")
//     stringParam("PARENT_BUILD","Skeleton_Application_Build","Parent build name")
//     stringParam("ENVIRONMENT_NAME","CI","Name of the environment.")
//   }
//   logRotator {
//     daysToKeep(logRotatorDaysToKeep)
//     numToKeep(logRotatorBuildNumToKeep)
//     artifactDaysToKeep(logRotatorArtifactsNumDaysToKeep)
//     artifactNumToKeep(logRotatorArtifactsNumToKeep)
//   }
//   scm scmProvider.get(projectFolderName, regressionTestGitRepo, "*/master", "adop-jenkins-master", null)
//   wrappers {
//     preBuildCleanup()
//     injectPasswords()
//     maskPasswords()
//     sshAgent("adop-jenkins-master")
//   }
//   environmentVariables {
//       env('WORKSPACE_NAME',workspaceFolderName)
//       env('PROJECT_NAME',projectFolderName)
//   }
//   label("docker")
//   steps {
//     shell('''## YOUR REGRESSION TESTING STEPS GO HERE'''.stripMargin())
//   }
// }