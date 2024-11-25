import pandas as pd
import joblib
from sklearn.metrics import accuracy_score, classification_report, roc_curve, auc
import matplotlib.pyplot as plt
from sklearn.base import ClassifierMixin
from typing import Tuple


def evaluate_model(model: ClassifierMixin, X: pd.DataFrame, y: pd.Series) -> Tuple[float, str]:
    """
    Оценка модели на данных.

    Args:
        model (ClassifierMixin): Заранее обученная модель для прогнозирования.
        X (pd.DataFrame): Выборка признаков.
        y (pd.Series): Выборка с целевыми значениями.

    Returns:
        Tuple[float, str]: Точность модели и отчёт по классификации.
    """
    predictions = model.predict(X)
    accuracy = accuracy_score(y, predictions)
    report = classification_report(y, predictions)
    return accuracy, report


def plot_roc_curve(model: ClassifierMixin, X_test: pd.DataFrame, y_test: pd.Series):
    """
    Построение ROC-кривой для модели.

    Args:
        model (ClassifierMixin): Обученная модель.
        X_test (pd.DataFrame): Тестовая выборка признаков.
        y_test (pd.Series): Тестовая выборка с целевыми значениями.
    """
    y_prob = model.predict_proba(X_test)[:, 1]
    fpr, tpr, _ = roc_curve(y_test, y_prob)
    roc_auc = auc(fpr, tpr)
    plt.figure()
    plt.plot(fpr, tpr, color='blue', lw=2, label=f'ROC curve (area = {roc_auc:.2f})')
    plt.plot([0, 1], [0, 1], color='grey', linestyle='--')
    plt.xlabel('False Positive Rate')
    plt.ylabel('True Positive Rate')
    plt.title('ROC Curve')
    plt.legend(loc="lower right")
    plt.show()


if __name__ == "__main__":
    try:
        # Загрузка данных
        data = pd.read_csv('data/customer_data_fe.csv')

        # Удаление столбца CustomerID, если он присутствует
        if 'CustomerID' in data.columns:
            data = data.drop(columns=['CustomerID'])

        # Разделение данных на признаки и целевую переменную
        X = data.drop(columns=['Churn'])
        y = data['Churn'].round().astype(int)

        # Загрузка модели
        model = joblib.load('models/churn_model.pkl')

        # Оценка модели
        accuracy, report = evaluate_model(model, X, y)
        print(f"Accuracy: {accuracy:.4f}")
        print("Classification Report:\n", report)

        # Построение ROC-кривой
        plot_roc_curve(model, X, y)

    except FileNotFoundError as e:
        print(f"Ошибка: {e}. Проверьте путь к файлам и попробуйте снова.")
    except Exception as e:
        print(f"Произошла непредвиденная ошибка: {e}")
