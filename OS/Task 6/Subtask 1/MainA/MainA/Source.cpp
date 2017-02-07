#include <Windows.h>
#include <stdio.h>

int APIENTRY WinMain(HINSTANCE hInstance, HINSTANCE hPrevInstance, LPSTR lpCmdLine, int nCmdShow) {
	STARTUPINFO si = { sizeof(si) };
	SECURITY_ATTRIBUTES saProcess, saThread;
	PROCESS_INFORMATION piProcessB,piProcessC;
	TCHAR szPath[MAX_PATH];
	

	saProcess.nLength = sizeof(saProcess);
	saProcess.lpSecurityDescriptor = NULL;
	saProcess.bInheritHandle = TRUE;

	saThread.nLength = sizeof(saThread);
	saThread.lpSecurityDescriptor = NULL;
	saThread.bInheritHandle = TRUE;

	lstrcpy(szPath, TEXT("ProcessB"));

	if (!CreateProcess(NULL, szPath, &saProcess, &saThread, TRUE, 0, NULL, NULL, &si, &piProcessB)) {
		MessageBox(NULL, TEXT("ProcessB failed"), TEXT("1:ProcessA"), MB_OK);
		return 0;
	}
	CloseHandle(piProcessB.hThread);


	sprintf(szPath, TEXT("ProcessC"));
	
	char procB[100];
	sprintf(procB, "%d", piProcessB.hProcess);
	SetEnvironmentVariable("PROCHANDLE_PROCB", procB);
	char procBID[100];
	sprintf(procBID, "%d", piProcessB.dwProcessId);
	SetEnvironmentVariable("PROCID_PROCB", procBID);

	if (!CreateProcess(NULL, szPath,NULL, NULL,TRUE,NORMAL_PRIORITY_CLASS | CREATE_SUSPENDED,NULL, NULL,&si, &piProcessC))
	{
		MessageBox(NULL, TEXT("ProcessC failed"), TEXT("1:ProcessA"), MB_OK);
		return(0);
	};


	Sleep(5000);

	if (1 == ResumeThread(piProcessC.hThread))

	{
		Sleep(5000);

		MessageBox(NULL, TEXT("ProcessC resumed"), TEXT("1:ProcessA"), MB_OK);
	}

	if (WAIT_FAILED == WaitForSingleObject(piProcessB.hProcess, INFINITE))
	{
		MessageBox(NULL, TEXT("WAIT_FAILED for ProcessB"), TEXT("2:ProcessA"), MB_OK);
	};

	
	if(WAIT_FAILED==WaitForSingleObject(piProcessC.hProcess,INFINITE))
	{
		MessageBox(NULL,TEXT("WAIT_FAILED for ProcessC"),TEXT("2:ProcessA"),MB_OK);
	};
	
	CloseHandle(piProcessB.hProcess);
	CloseHandle(piProcessB.hThread);

	CloseHandle(piProcessC.hProcess);
//	CloseHandle(piProcessC.hThread);
	MessageBox(NULL, TEXT("ProcessA finished"), TEXT("2:ProcessA"), MB_OK);
	return 0;
}