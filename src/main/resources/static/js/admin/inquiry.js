const searchForm = document.getElementById('searchForm');

const btnSearch = document.querySelector('.btn-search');

const type = document.querySelector('.type');

const options = document.querySelectorAll('.option');

options.forEach(option => {
    if(option.value === type.value) {
        option.setAttribute("selected", "");
    }
})

btnSearch.addEventListener('click', function () {
    const type = searchForm['type'].value;
    const keyword = searchForm['keyword'].value;

    axios.get('/admin/inquiryList', {params: {type: type, keyword: keyword}})
        .then(res => {
            if(res.data.success) {
                location.href = "/admin/inquiryList?type=" + type + "&keyword=" + keyword;
            }
        })
        .catch(err => {
            console.log(err);
        });
})
