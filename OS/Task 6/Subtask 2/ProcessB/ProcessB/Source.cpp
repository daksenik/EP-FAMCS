#include <iostream>

using namespace std;

int main()
{
	int i = 0;
	while (true) {
		printf("process B... %d\n", i++);
		if (i > 1e9)i = 0;
	}
}