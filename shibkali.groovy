node('win-build') {
    checkout scm   
    currentBuild.result = 'SUCCESS'

    stage('build') {
        def msbuild =  "C:\Program Files (x86)\Microsoft Visual Studio\2019\Community\MSBuild\Current\Bin\msbuild.exe"
        def slnfile = "TRA e-Services Manager.sln"
        def exitStatus = bat(returnStatus: true, script: "${msbuild} ${slnfile} /p:Configuration=Debug")
        if (exitStatus != 0){
            currentBuild.result = 'FAILURE'
            error 'build failed'
        }
    }

    stage('upload artifacts') {
        if (currentBuild.result = 'SUCCESS') {
            sh """
            git add --all
            git commit -m \"Check in the build artifacts\"
            git push 
            """
        }

    }


}
