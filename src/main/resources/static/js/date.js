// Важно подключать скрипт в конце HTML, иначе выдает ошибку!

const monthSelect = document.getElementById("month");
const daySelect = document.getElementById("day");

const months = ['Январь', 'Февраль', 'Март', 'Апрель',
'Май', 'Июнь', 'Июль', 'Август', 'Сентябрь', 'Октябрь',
'Ноябрь', 'Декабрь'];

(function populateMonths(){
    for(let i = 0; i < months.length; i++){
        const option = document.createElement('option');
        option.textContent = months[i];
        monthSelect.appendChild(option);
    }
    monthSelect.value = "Январь";
})();

let previousDay;

function populateDays(month){

    while(daySelect.firstChild){
        daySelect.removeChild(daySelect.firstChild);
    }

    let dayNum;

    if(month === 'Январь' || month === 'Март' ||
    month === 'Май' || month === 'Июль' || month === 'Август'
    || month === 'Октябрь' || month === 'Декабрь') {
        dayNum = 31;
    } else if(month === 'Апрель' || month === 'Июнь'
    || month === 'Сентябрь' || month === 'Ноябрь') {
        dayNum = 30;
    }else{
        dayNum = 28;
    }
    for(let i = 1; i <= dayNum; i++){
        const option = document.createElement("option");
        option.textContent = i;
        daySelect.appendChild(option);
    }
    if(previousDay){
        daySelect.value = previousDay;
        if(daySelect.value === ""){
            daySelect.value = previousDay - 1;
        }
        if(daySelect.value === ""){
            daySelect.value = previousDay - 2;
        }
        if(daySelect.value === ""){
            daySelect.value = previousDay - 3;
        }
    }
}

populateDays(monthSelect.value);

monthSelect.onchange = function() {
    populateDays(monthSelect.value);
}
daySelect.onchange = function() {
    previousDay = daySelect.value;
}