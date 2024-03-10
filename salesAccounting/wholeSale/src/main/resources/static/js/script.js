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
});

