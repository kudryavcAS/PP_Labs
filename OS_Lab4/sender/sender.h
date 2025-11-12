#pragma once

#include <windows.h>
#include <iostream>
#include <string>
#include <cstring>

const int MAX_MESSAGE_LENGTH = 20;  // Максимальная длина сообщения
const int MAX_MESSAGES = 100;       // Максимальный размер буфера

// Структура для хранения одного сообщения
struct Message {
	char content[MAX_MESSAGE_LENGTH + 1];  // Текст сообщения + нулевой символ
};

// Структура разделяемых данных между процессами (должна совпадать с Receiver)
struct SharedData {
	Message messages[MAX_MESSAGES];  // Кольцевой буфер сообщений
	int readIndex;                   // Индекс для чтения (позиция Receiver)
	int writeIndex;                  // Индекс для записи (позиция Sender)
	int messageCount;                // Текущее количество сообщений в буфере
	int maxMessages;                 // Максимальная емкость буфера
	// Примечание: HANDLE'ы семафоров и мьютекса хранятся здесь для единообразия,
	// но в Windows они должны быть именованными для доступа из разных процессов
};