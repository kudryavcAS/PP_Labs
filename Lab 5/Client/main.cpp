#include "Client.hpp"

int main() {
	HANDLE hPipe = connectToServer();

	if (hPipe == INVALID_HANDLE_VALUE) {
		std::cerr << "Failed to connect to server. Error: " << GetLastError() << "\n";
		(void)_getch();
		return 1;
	}

	processSession(hPipe);

	CloseHandle(hPipe);
	return 0;
}