document.addEventListener('DOMContentLoaded', function() {

    var searchButton = document.getElementById('button-search');
    var searchInput = document.getElementById('search');

    if (searchButton) {
        searchButton.addEventListener('click', function() {
            // Запуск поиска при клике на иконку лупы
            document.querySelector('form').submit();
        });
    }

    if (searchInput) {
        searchInput.addEventListener('keypress', function(event) {
            if (event.key === 'Enter') {
                // Предотвращаем отправку формы по Enter
                event.preventDefault();
                // Запуск поиска при нажатии Enter
                document.querySelector('form').submit(); // Запуск поиска при нажатии Enter
            }
        });
    }
        // var selectedProductId;
        //
        // document.querySelectorAll('.product-item').forEach(function(item) {
        //     item.addEventListener('click', function() {
        //         selectedProductId = this.getAttribute('data-id');
        //         document.querySelectorAll('.product-item').forEach(function(el) {
        //             el.classList.remove('selected');
        //         });
        //         this.classList.add('selected');
        //     });
        // });
        //
        // var cloneButton = document.getElementById('cloneProductButton');
        // if(cloneButton) {
        //     cloneButton.addEventListener('click', function() {
        //         if(selectedProductId) {
        //             window.location.href = `/product/clone/${selectedProductId}`;
        //         } else {
        //             alert('Выберите продукт для копирования');
        //         }
        //     });
        // }

        // var deleteButton = document.getElementById('deleteProductButton');
        // if(deleteButton) {
        //     deleteButton.addEventListener('click', function() {
        //         if(selectedProductId) {
        //             if(confirm('Вы уверены, что хотите удалить выбранный продукт?')) {
        //                 window.location.href = `/product/delete/${selectedProductId}`;
        //             }
        //         } else {
        //             alert('Выберите продукт для удаления');
        //         }
        //     });
        // }

    var selectedProduct = null;

    document.querySelectorAll('.product-item').forEach(function(item) {
        // Обработка одиночного клика для выделения товара
        item.addEventListener('click', function() {
            if (selectedProduct) {
                selectedProduct.classList.remove('selected'); // Снимаем выделение с предыдущего выбранного товара
            }
            this.classList.add('selected'); // Выделяем текущий товар
            selectedProduct = this; // Сохраняем выбранный товар
        });

        // Обработка двойного клика для перехода на страницу детализации товара
        item.addEventListener('dblclick', function() {
            var productId = this.dataset.id; // ID товара сохранен в атрибуте data-id
            window.location.href = `/product/${productId}`; // Перенаправление на страницу детализации товара
        });
    });

    var cloneButton = document.getElementById('cloneProductButton');
    var deleteButton = document.getElementById('deleteProductButton');
    cloneButton.addEventListener('click', function(event) {
        event.preventDefault(); // Предотвращаем стандартное поведение
        if(selectedProductId) {
            window.location.href = `/product/save`;
        } else {
            alert('Выберите продукт для копирования');
        }
    });

    deleteButton.addEventListener('click', function(event) {
        event.preventDefault(); // Предотвращаем стандартное поведение
        if(selectedProductId) {
            if(confirm('Вы уверены, что хотите удалить выбранный продукт?')) {
                window.location.href = `/product/delete/${selectedProductId}`;
            }
        } else {
            alert('Выберите продукт для удаления');
        }
    });
});

document.getElementById('createProductButton').addEventListener('click', function(event) {
    window.location.href = '/product/edit/{id}'; // Путь к странице создания нового продукта
});

// Добавляем адрес, на который должна быть отправлена форма
document.querySelector('form').addEventListener('submit', function(event) {
    event.preventDefault();
    var action = this.getAttribute('action');
    window.location.href = action + '?name=' + searchInput.value;
});


