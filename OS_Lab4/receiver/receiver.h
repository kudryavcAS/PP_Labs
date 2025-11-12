#ifndef RECEIVER_H
#define RECEIVER_H

#include <windows.h>
#include <iostream>
#include <string>
#include <vector>
#include <climits>

const int MAX_MESSAGE_LENGTH = 20;  // Максимальная длина сообщения
const int MAX_MESSAGES = 100;       // Максимальный размер буфера

// Структура для хранения одного сообщения
struct Message {
   char content[MAX_MESSAGE_LENGTH + 1];  // Текст сообщения + нулевой символ
};

// Структура разделяемых данных между процессами
// Только данные, без HANDLE'ов (они создаются отдельно как именованные объекты)
struct SharedData {
   Message messages[MAX_MESSAGES];  // Кольцевой буфер сообщений
   int readIndex;                   // Индекс для чтения (позиция Receiver)
   int writeIndex;                  // Индекс для записи (позиция Sender)
   int messageCount;                // Текущее количество сообщений в буфере
   int maxMessages;                 // Максимальная емкость буфера
};

// Функция для ввода натурального числа с валидацией
void inputNatural(int& integer, int max = INT_MAX);
bool startProcess(const std::string& processPath, const std::string& arguments, const std::string& windowTitle = "");
#endif