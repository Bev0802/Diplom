document.addEventListener('DOMContentLoaded', function () {

    var searchButton = document.getElementById('button-search');
    var searchInput = document.getElementById('search');

    if (searchButton) {
        searchButton.addEventListener('click', function () {
            // Запуск поиска при клике на иконку лупы
            document.querySelector('form').submit();
        });
    }

    if (searchInput) {
        searchInput.addEventListener('keypress', function (event) {
            if (event.key === 'Enter') {
                // Предотвращаем отправку формы по Enter
                event.preventDefault();
                // Запуск поиска при нажатии Enter
                document.querySelector('form').submit(); // Запуск поиска при нажатии Enter
            }
        });
    }
    var selectedProduct = null;

    document.querySelectorAll('.product-item').forEach(function (item) {
        // Обработка одиночного клика для выделения товара
        item.addEventListener('click', function () {
            if (selectedProduct) {
                selectedProduct.classList.remove('selected'); // Снимаем выделение с предыдущего выбранного товара
            }
            this.classList.add('selected'); // Выделяем текущий товар
            selectedProduct = this; // Сохраняем выбранный товар
        });

        // Обработка двойного клика для перехода на страницу детализации товара
        item.addEventListener('dblclick', function () {
            var productId = this.dataset.id; // ID товара сохранен в атрибуте data-id
            window.location.href = `/product/${productId}`; // Перенаправление на страницу детализации товара
        });
    });

    var cloneButton = document.getElementById('cloneProductButton');
    var deleteButton = document.getElementById('deleteProductButton');
    cloneButton.addEventListener('click', function (event) {
        event.preventDefault(); // Предотвращаем стандартное поведение
        if (selectedProductId) {
            window.location.href = `/product/save`;
        } else {
            alert('Выберите продукт для копирования');
        }
    });

    deleteButton.addEventListener('click', function (event) {
        event.preventDefault(); // Предотвращаем стандартное поведение
        if (selectedProductId) {
            if (confirm('Вы уверены, что хотите удалить выбранный продукт?')) {
                window.location.href = `/product/delete/${selectedProductId}`;
            }
        } else {
            alert('Выберите продукт для удаления');
        }
    });
});

document.getElementById('createProductButton').addEventListener('click', function (event) {
    window.location.href = '/product/edit/{id}'; // Путь к странице создания нового продукта
});

// Добавляем адрес, на который должна быть отправлена форма
document.querySelector('form').addEventListener('submit', function (event) {
    event.preventDefault();
    var action = this.getAttribute('action');
    window.location.href = action + '?name=' + searchInput.value;
});


//     function handleLogin() {
//     var inn = document.getElementById('inn').value;
//     var password = document.getElementById('exampleInputPassword1').value;
//     // Здесь должна быть ваша логика аутентификации (AJAX запрос к /api/organizations/authenticate)
//
//     // Псевдокод для демонстрации перенаправления
//     if (аутентификацияУспешна) {
//     window.location.href = "/organization/employeeLogin"; // Перенаправление на форму входа сотрудника
// } else if (галочкаУстановлена || организацияНеСуществует) {
//     window.location.href = "/organization/register"; // Перенаправление на форму регистрации организации
// } else {
//     // Обработка ошибок или другие действия




