import pandas as pd
from sklearn.preprocessing import StandardScaler

def feature_engineering(df: pd.DataFrame) -> pd.DataFrame:
    """
    Создание новых признаков и масштабирование данных.

    Args:
        df (pd.DataFrame): Исходные данные для создания признаков и масштабирования.

    Returns:
        pd.DataFrame: Данные с новыми и масштабированными признаками.
    """
    # Проверка на наличие нужных столбцов
    if 'NumTransactions' in df.columns and 'Tenure' in df.columns:
        # Создание нового признака "TotalTransactions"
        df['TotalTransactions'] = df['NumTransactions'] * df['Tenure']
    else:
        raise KeyError("Отсутствуют необходимые столбцы для создания нового признака: 'NumTransactions' и 'Tenure'")

    # Масштабирование числовых признаков
    scaler = StandardScaler()
    numeric_features = df.select_dtypes(include=['float64', 'int64']).columns
    df[numeric_features] = scaler.fit_transform(df[numeric_features])

    return df

if __name__ == "__main__":
    # Загрузка данных
    data = pd.read_csv('data/customer_data_preprocessed.csv')

    # Создание признаков и масштабирование
    if data.shape[0] < 5:
        print("Внимание: Данных слишком мало для надежной обработки и обучения.")
    data = feature_engineering(data)

    # Сохранение обработанных данных
    data.to_csv('data/customer_data_fe.csv', index=False)
    print("Обработка признаков завершена и данные сохранены в 'data/customer_data_fe.csv'.")
