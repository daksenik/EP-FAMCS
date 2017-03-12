#include <iostream>
#include <fstream>
#include <Windows.h>
#include <string>
using namespace std;

bool stop_interface, terminateCounter;
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
        if (terminateCounter) {
          stop_interface = true;
          return result;
        }
        result[i][j] += a[i][k] * a[k][j];
        progress += 1 / pow(n, 3);
      }
  return result;
}

DWORD WINAPI counterThread(LPVOID param) {
  const int SIZE = 5000;
  int**a = new int*[SIZE];
  int**b = new int*[SIZE];
  for (int i = 0; i < SIZE; i++) {
    a[i] = new int[SIZE];
    b[i] = new int[SIZE];
  }

  for(int i=0;i<SIZE;i++)
    for (int j = 0; j < SIZE; j++)
      a[i][j] = b[i][j] = i == j ? 1 : 0;

  ofstream out("output.txt");

  int**ans = calculate(a, b, SIZE);
  for (int i = 0; i < SIZE; i++) {
    if (terminateCounter) {
      stop_interface = true;
      return 0;
    }
    for (int j = 0; j < SIZE; j++)
      out << ans[i][j] << " ";
    out << endl;
  }
  stop_interface = true;
  return 0;
}

DWORD WINAPI interfaceThread(LPVOID param) {
  cout << "Counting..." << endl;
  double lastValue = progress;
  string s;
  while (!stop_interface) {
    cin >> s;
    if (s == "terminate")terminateCounter = true;
  }
  printf("%.2f%%\n", progress * 100);
  return 0;
}

int main() {
  stop_interface = terminateCounter = false;
  progress = 0;
  HANDLE counter_thread,interface_thread;
  DWORD counterId,interfaceId;
  interface_thread = CreateThread(NULL, 0, interfaceThread, NULL, 0, &interfaceId);
  counter_thread = CreateThread(NULL, 0, counterThread, NULL, 0, &counterId);
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