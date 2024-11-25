package com.bank.common.utils;

import com.bank.common.exceptions.ApplicationRuntimeException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Утилита для работы с файлами
 */
public class FileUtils {

    /**
     * Конструктор утилиты недоступен потребителям
     */
    private FileUtils() {
    }

    /**
     * Читает все строки из файла в ресурсах
     *
     * @param name имя файла
     * @return строки с содержимым файла
     * @throws ApplicationRuntimeException если файл не существует или произошла ошибка чтения
     */
    public static List<String> readAllLinesFromResource(String name) {
        InputStream inputStream = Optional.ofNullable(name)
            .map(e -> FileUtils.class.getClassLoader().getResourceAsStream(e))
            .orElseThrow(() -> new ApplicationRuntimeException("Resource not found: " + name));
        try (var reader = new BufferedReader(new InputStreamReader(inputStream))) {
            List<String> lines = new ArrayList<>();
            for (; ; ) {
                String line = reader.readLine();
                if (line == null) {
                    break;
                }
                lines.add(line);
            }
            return lines;
        } catch (IOException e) {
            throw new ApplicationRuntimeException("Can't read resource: " + name, e);
        }
    }
}
