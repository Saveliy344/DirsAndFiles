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
}


function addFileToTable(file){
    const table = document.getElementById();
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
    const sizeInMB = (directory.filesSize / 1024).toFixed(2);
    sizeCell.textContent = `${sizeInMB} MB`;

    const actionsCell = newRow.insertCell(5);
    const filesButton = document.createElement("button");
    filesButton.textContent = "Файлы";
    filesButton.classList.add("files-btn");
    filesButton.id = "files-btn " + directory.id;
    filesButton.addEventListener("click", function(){
        makeDialogVisible();
    });
    actionsCell.appendChild(filesButton);
}

function loadDirectories() {
    fetch("http://localhost:8080/daf/directories")
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

window.onload = loadDirectories;

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
                clearTable("directoriesTable");
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
