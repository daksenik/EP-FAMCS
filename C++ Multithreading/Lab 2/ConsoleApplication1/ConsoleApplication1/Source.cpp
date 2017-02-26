#include <iostream>
#include <Windows.h>
using namespace std;

bool stop_interface;
double progress;

int** calculate(int** a, int** b, int n) {
  int** result = new int*[n];
  for (int i = 0; i < n; i++)result[i] = new int[n];

  for (int i = 0; i < n; i++)
    for (int j = 0; j < n; j++)
      result[i][j] = 0;

  for (int i = 0; i < n; i++)
    for (int j = 0; j < n; j++)
      for (int k = 0; k < n; k++) {
        result[i][j] += a[i][k] * a[k][j];
        progress += 1 / pow(n, 3);
      }
  return result;
}

DWORD WINAPI counterThread(LPVOID param) {
  int**a = new int*[100];
  int**b = new int*[100];
  for (int i = 0; i < 100; i++) {
    a[i] = new int[100];
    b[i] = new int[100];
  }

  for(int i=0;i<100;i++)
    for (int j = 0; j < 100; j++)
      a[i][j] = b[i][j] = i == j ? 1 : 0;

  int**ans = calculate(a, b, 100);
  for (int i = 0; i < 100; i++) {
    for (int j = 0; j < 100; j++)
      cout << ans[i][j] << " ";
    cout << endl;
  }
  stop_interface = true;
  return 0;
}

DWORD WINAPI interfaceThread(LPVOID param) {
  cout << "Counting..." << endl;
  double lastValue = progress;
  while (!stop_interface)if (progress - lastValue > 0.01) {
    printf("%.2f%%\n", progress*100);
    lastValue = progress;
  }
  printf("%.2f%%\n", progress * 100);
  return 0;
}

int main() {
  stop_interface = false;
  progress = 0;
  HANDLE counter_thread,interface_thread;
  DWORD counterId,interfaceId;
  interface_thread = CreateThread(NULL, 0, interfaceThread, NULL, 0, &interfaceId);
  cout << "1" << endl;
  counter_thread = CreateThread(NULL, 0, counterThread, NULL, 0, &counterId);
  cout << "2" << endl;
  if (WaitForSingleObject(counter_thread, INFINITE) == WAIT_FAILED) {
    cout << "WAIT FAILED for counter thread" << endl;
  } else {
    if (!CloseHandle(counter_thread)) {
      cout << "CLOSE HANDLE FAILED for counter thread" << endl;
    } else {
      cout << "Counter thread finished" << endl;
    }
  }

  if (WaitForSingleObject(interface_thread, INFINITE) == WAIT_FAILED) {
    cout << "WAIT FAILED for interface thread" << endl;
  } else {
    if (!CloseHandle(interface_thread)) {
      cout << "CLOSE HANDLE FAILED for interface thread" << endl;
    }
    else {
      cout << "Interface thread finished" << endl;
    }
  }
}