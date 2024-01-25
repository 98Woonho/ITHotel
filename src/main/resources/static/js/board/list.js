const a_click = document.querySelectorAll('.tex');
const content = document.querySelectorAll('.content');

a_click.forEach(function (a) {
    a.addEventListener('click', function () {

        const index = Array.from(a_click).indexOf(a);
        const targetContent = content[index];

        if (!targetContent.classList.contains('show'))
            targetContent.classList.add('show');
        else
            targetContent.classList.remove('show');
    });
});