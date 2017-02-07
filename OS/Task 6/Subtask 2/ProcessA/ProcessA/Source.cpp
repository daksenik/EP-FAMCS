#include <Windows.h>
#include <stdio.h>
#include <conio.h>

using namespace std;

int main()
{
	STARTUPINFO si = { sizeof(si) };
	SECURITY_ATTRIBUTES saProcess, saThread;
	PROCESS_INFORMATION piProcessB, piProcessC;
	TCHAR szPath[MAX_PATH];


	saProcess.nLength = sizeof(saProcess);
	saProcess.lpSecurityDescriptor = NULL;
	saProcess.bInheritHandle = FALSE;

	saThread.nLength = sizeof(saThread);
	saThread.lpSecurityDescriptor = NULL;
	saThread.bInheritHandle = FALSE;

	lstrcpy(szPath, TEXT("ProcessB"));

	if (!CreateProcess(NULL, szPath, &saProcess, &saThread, FALSE, 0, NULL, NULL, &si, &piProcessB)) {
		MessageBox(NULL, TEXT("ProcessB failed"), TEXT("1:ProcessA"), MB_OK);
		return 0;
	}

	
	DWORD dwApid = GetCurrentProcessId();
	sprintf(szPath, TEXT("ProcessC %i %i"), piProcessB.hProcess, dwApid);

	if (!CreateProcess(NULL, szPath, NULL, NULL,TRUE,NORMAL_PRIORITY_CLASS | CREATE_SUSPENDED,NULL, NULL, &si, &piProcessC))
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

	if (WAIT_FAILED == WaitForSingleObject(piProcessB.hProcess, INFINITE)) {
		MessageBox(NULL, TEXT("WAIT_FAILED for ProcessB "), TEXT("1:ProcessA"), MB_OK);
	};
	
	CloseHandle(piProcessB.hProcess);
	CloseHandle(piProcessB.hThread);

	CloseHandle(piProcessC.hProcess);
	CloseHandle(piProcessC.hThread);
	MessageBox(NULL, TEXT("ProcessA finished"), TEXT("2:ProcessA"), MB_OK);
	return 0;
}