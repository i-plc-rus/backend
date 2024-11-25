import pandas as pd
from sklearn.preprocessing import StandardScaler
from typing import Tuple

def load_data(file_path: str) -> pd.DataFrame:
    """
    Загрузка данных из CSV-файла.

    Args:
        file_path (str): Путь к CSV-файлу с данными.

    Returns:
        pd.DataFrame: Загруженные данные в формате DataFrame.
    """
    return pd.read_csv(file_path)

def preprocess_data(df: pd.DataFrame) -> pd.DataFrame:
    """
    Предобработка данных: обработка пропусков, преобразование категориальных признаков и масштабирование.

    Args:
        df (pd.DataFrame): Исходные данные для предобработки.

    Returns:
        pd.DataFrame: Предобработанные данные.
    """
    # Заполнение пропусков медианой для числовых данных и forward/backward fill для остальных
    df = df.fillna(df.median(numeric_only=True))  # Заполняем числовые пропуски медианой
    df = df.ffill().bfill()  # Остальные пропуски заполняем

    # Преобразование категориальных признаков
    df = pd.get_dummies(df, drop_first=True)

    # Масштабирование числовых признаков
    scaler = StandardScaler()
    numeric_features = df.select_dtypes(include=['float64', 'int64']).columns
    df[numeric_features] = scaler.fit_transform(df[numeric_features])

    return df

if __name__ == "__main__":
    # Загрузка данных
    data = load_data('data/customer_data.csv')

    # Предобработка данных
    data = preprocess_data(data)

    # Сохранение предобработанных данных для обучения модели
    data.to_csv('data/customer_data_preprocessed.csv', index=False)
