import pandas as pd
import joblib
from sklearn.base import ClassifierMixin
from typing import Any, List


def load_model(model_path: str) -> ClassifierMixin:
    """
    Загрузка обученной модели из файла.

    Args:
        model_path (str): Путь к файлу с сохраненной моделью.

    Returns:
        ClassifierMixin: Загруженная модель.
    """
    return joblib.load(model_path)


def predict_churn(model: ClassifierMixin, new_data: pd.DataFrame) -> Any:
    """
    Прогноз оттока для новых данных.

    Args:
        model (ClassifierMixin): Обученная модель.
        new_data (pd.DataFrame): Новые данные для прогнозирования.

    Returns:
        Any: Предсказания модели для новых данных.
    """
    predictions = model.predict(new_data)
    return predictions


def check_required_columns(df: pd.DataFrame, required_columns: List[str]) -> bool:
    """
    Проверка наличия необходимых столбцов в DataFrame.

    Args:
        df (pd.DataFrame): Данные для проверки.
        required_columns (List[str]): Список требуемых столбцов.

    Returns:
        bool: True, если все требуемые столбцы присутствуют, иначе False.
    """
    missing_columns = [col for col in required_columns if col not in df.columns]
    if missing_columns:
        print(f"Предупреждение: Отсутствуют необходимые столбцы для предсказания: {missing_columns}")
        return False
    return True


if __name__ == "__main__":
    try:
        # Загрузка модели
        model = load_model('models/churn_model.pkl')

        # Загрузка данных для предсказания
        data = pd.read_csv('data/customer_data_fe.csv')

        # Проверка наличия необходимых столбцов
        required_columns = ['NumTransactions', 'Tenure', 'MonthlyCharges', 'TotalCharges', 'TotalTransactions']
        if not check_required_columns(data, required_columns):
            raise ValueError("Недостаточно данных для выполнения прогноза.")

        # Создание нового DataFrame с нужными признаками
        new_data = data[required_columns]

        # Проверка, что данные не слишком малы для выполнения прогноза
        if new_data.shape[0] < 3:
            print("Предупреждение: Данных слишком мало для уверенного прогноза.")

        # Прогноз оттока
        predictions = predict_churn(model, new_data)

        # Сохранение предсказаний в файл
        data['ChurnPrediction'] = predictions
        data.to_csv('data/customer_data_predictions.csv', index=False)

        print("Предсказания успешно сохранены в 'data/customer_data_predictions.csv'.")

    except FileNotFoundError as e:
        print(f"Ошибка: {e}. Проверьте путь к файлам и попробуйте снова.")
    except ValueError as e:
        print(f"Ошибка: {e}")
    except Exception as e:
        print(f"Произошла непредвиденная ошибка: {e}")
