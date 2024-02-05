const question = document.querySelectorAll('.question_li');
const answer = document.querySelectorAll('.answer_li');

question.forEach(function (a) {
    a.addEventListener('click', function () {

        const index = Array.from(question).indexOf(a);
        const targetContent = answer[index];

        if (!targetContent.classList.contains('show'))
            targetContent.classList.add('show');
        else
            targetContent.classList.remove('show');
    });
});