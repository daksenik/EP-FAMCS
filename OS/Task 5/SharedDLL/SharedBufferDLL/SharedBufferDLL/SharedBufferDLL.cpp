#include "SharedBufferDLL.h"

#pragma data_seg("SharedSection")
__declspec(dllexport) bool bIsMessageInBuffer = false;
__declspec(dllexport) WCHAR wcSharedBufferDLL[500] = { 0, };
#pragma data_seg()

#pragma comment(linker,"/Section:SharedSection,RWS")