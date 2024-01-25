<h2>Менеджер задач</h2>
Приложение позволяет ставить задачи зарегистрированным пользователям.



### Hexlet tests and linter status:
[![Actions Status](https://github.com/JavaQuaker/java-project-99/actions/workflows/hexlet-check.yml/badge.svg)](https://github.com/JavaQuaker/java-project-99/actions)
<a href="https://codeclimate.com/github/JavaQuaker/java-project-99/maintainability"><img src="https://api.codeclimate.com/v1/badges/9f5cd3acdaf5efef4d2f/maintainability" /></a>
<a href="https://codeclimate.com/github/JavaQuaker/java-project-99/test_coverage"><img src="https://api.codeclimate.com/v1/badges/9f5cd3acdaf5efef4d2f/test_coverage" /></a>
[Render review](https://java-project-99-xpyl.onrender.com)
[documentation](http://java-project-99-xpyl.onrender.com/swagger-ui/index.html)


<button id="copyButton">Копировать</button>
<br>
<textarea id="copyText" rows="4" cols="50">
    username: hexlet@example.com
    password: qwerty
</textarea>

<script>
    document.getElementById('copyButton').addEventListener('click', function() {
        var copyText = document.getElementById('copyText');
        copyText.select();
        document.execCommand('copy');
        alert('Текст скопирован: ' + copyText.value);
    });
</script>

