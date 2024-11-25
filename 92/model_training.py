import os
import pandas as pd
import numpy as np
from sklearn.linear_model import LogisticRegression
from sklearn.model_selection import GridSearchCV
import joblib
from typing import Any


def train_model(X_train: pd.DataFrame, y_train: pd.Series) -> Any:
    """
    Обучение модели LogisticRegression с использованием Grid Search для подбора гиперпараметра C.

    Args:
        X_train (pd.DataFrame): Обучающая выборка признаков.
        y_train (pd.Series): Обучающая выборка с целевыми значениями.

    Returns:
        Any: Наилучшая модель по результатам Grid Search.
    """
    param_grid = {'C': [0.01, 0.1, 1, 10, 100]}
    grid_search = GridSearchCV(LogisticRegression(random_state=42, max_iter=1000), param_grid, cv=2)
    grid_search.fit(X_train, y_train)
    print("Лучшие параметры:", grid_search.best_params_)
    return grid_search.best_estimator_


if __name__ == "__main__":
    try:
        # Создание директории 'models', если она не существует
        os.makedirs('models', exist_ok=True)

        # Загрузка данных
        X_train = pd.read_csv('data/customer_data_fe.csv')

        # Удаление столбца CustomerID, если он присутствует
        if 'CustomerID' in X_train.columns:
            X_train = X_train.drop(columns=['CustomerID'])

        # Выделение целевой переменной
        y_train = X_train['Churn']
        X_train = X_train.drop(columns=['Churn'])

        # Проверка и преобразование целевой переменной
        y_train = y_train.round().astype(int)
        unique, counts = np.unique(y_train, return_counts=True)
        print("Распределение классов в y_train:", dict(zip(unique, counts)))

        # Обучение модели
        model = train_model(X_train, y_train)

        # Сохранение модели
        joblib.dump(model, 'models/churn_model.pkl')
        print("Модель успешно сохранена в 'models/churn_model.pkl'.")

    except FileNotFoundError as e:
        print(f"Ошибка: {e}. Проверьте путь к файлам и попробуйте снова.")
    except Exception as e:
        print(f"Произошла непредвиденная ошибка: {e}")
