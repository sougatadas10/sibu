node('win-build') {
    checkout scm   
    
    stage('clone code') { //This stage should be avoided if build script resides on same repo as project code
        git clone git@bitbucket.org:sougatadas10/merger.git //make sure ssh keys are added in jenkins
    }

    stage('build') {
        def msbuild =  "path/to/msbuild/MsBuild.exe"
        def exitStatus = bat(returnStatus: true, script: "${msbuild} solution.sln /p:Configuration=Debug")
        if (exitStatus != 0){
            currentBuild.result = 'FAILURE'
            error 'build failed'
        }
    }

    stage('upload artifacts') {
        //Install Jenkins Artifactory Plug-in
        def server = Artifactory.newServer url: 'artifactory-url', username: 'username', password: 'password'
        def uploadSpec = """{
            "files": [
                {
                    "pattern": "bazinga/*froggy*.zip",
                    "target": "bazinga-repo/froggy-files/"
                }
            ]
        }"""
        server.upload spec: uploadSpec
    }


}