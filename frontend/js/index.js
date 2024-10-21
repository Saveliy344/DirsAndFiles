function clearTable(tableName) {
    const tableBody = document.getElementById(tableName).getElementsByTagName("tbody")[0];
    while (tableBody.rows.length > 0) {
        tableBody.deleteRow(0);
    }
}

function makeDialogVisible(){
    const dialog = document.getElementById("filesModal");
    dialog.style.display = 'block';
}

function closeDialog() {
    const dialog = document.getElementById("filesModal");
    dialog.style.display = 'none';
    clearTable("filesTable");
}

//Получить размер с точностью от 0 до 1023 Байт, КБайт, МБайт и т.д.
function getStringSize(size) {
    const prefixes = ["Б", "КБ", "МБ", "ГБ", "ТБ", "ПБ"];

    let thousandsDegree = 0;

    while (size >= 1024 && thousandsDegree < prefixes.length - 1) {
        size /= 1024;
        thousandsDegree++;
    }

    size = size.toFixed(2);

    return `${size} ${prefixes[thousandsDegree]}`;
}

function addFileToTable(file){
    const table = document.getElementById("filesTable").getElementsByTagName("tbody")[0];
    const newRow = table.insertRow();
    
    const nameCell = newRow.insertCell(0);
    nameCell.textContent = file.name;
    
    const sizeCell = newRow.insertCell(1);
    if (file.isDir)
        sizeCell.textContent = "<DIR>";
    else
        sizeCell.textContent = getStringSize(file.size);
}

function addDirectoryToTable(directory) {
    const table = document.getElementById("directoriesTable").getElementsByTagName("tbody")[0];
    const newRow = table.insertRow();

    const dateCell = newRow.insertCell(0);
    const date = new Date(directory.dateOfAdd).toLocaleString('ru-RU', {
        year: 'numeric',
        month: 'numeric',
        day: 'numeric',
        hour: 'numeric',
        minute: 'numeric',
        second: 'numeric'
    });
    dateCell.textContent = date;

    const baseDirCell = newRow.insertCell(1);
    baseDirCell.textContent = directory.name;

    const dirCountCell = newRow.insertCell(2);
    dirCountCell.textContent = directory.dirsCount;

    const filesCountCell = newRow.insertCell(3);
    filesCountCell.textContent = directory.filesCount;

    const sizeCell = newRow.insertCell(4);
    sizeCell.textContent =  getStringSize(directory.filesSize);

    const actionsCell = newRow.insertCell(5);
    const filesButton = document.createElement("button");
    filesButton.textContent = "Файлы";
    filesButton.classList.add("files-btn");
    filesButton.id = "files-btn " + directory.id;
    filesButton.addEventListener("click", function(){
        makeDialogVisible();
        fetch("http://localhost:8080/daf/filesAndDirs/" + directory.id)
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then(data => {
            data.forEach(file => {
                addFileToTable(file);
            });
        })
        .catch(error => {
            console.error('Ошибка при загрузке файлов:', error);
        });
    });
    actionsCell.appendChild(filesButton);
}

function loadDirectories(asc = false) {
    clearTable("directoriesTable");
    // Определяем URL для запроса в зависимости от значения параметра asc
        const url = asc ? "http://localhost:8080/daf/directories/asc" : "http://localhost:8080/daf/directories";

    fetch(url)
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then(data => {
            data.forEach(directory => {
                addDirectoryToTable(directory);
            });
        })
        .catch(error => {
            console.error('Ошибка при загрузке директорий:', error);
        });
}

window.onload = function(){
    loadDirectories(false);
};

document.getElementById("addBtn").addEventListener("click", function() {
    const directoryName = document.getElementById("directory").value;

    if (directoryName) {
        const requestBody = JSON.stringify({ name: directoryName });

        fetch("http://localhost:8080/daf/addDirectory", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: requestBody
        })
        .then(response => {
            if (response.ok) {
                loadDirectories();
                document.getElementById("directory").value = "";
            } else {
                throw new Error('Ошибка при добавлении директории');
            }
        })
        .catch(error => {
            console.error('Ошибка:', error);
            alert('Не удалось добавить директорию');
        });
    } else {
        alert("Введите название директории");
    }
});

document.getElementById("close").addEventListener("click", function(){
    closeDialog();
});


document.getElementById("sort").addEventListener("click", function(){
    const sortArrow = document.getElementById('sort');

        if (sortArrow.innerHTML === '▲') {
            sortArrow.innerHTML = '▼';
            loadDirectories(false);
        } else {
            sortArrow.innerHTML = '▲';
            loadDirectories(true);
        }
})