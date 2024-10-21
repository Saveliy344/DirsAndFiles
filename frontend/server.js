const express = require('express');
const app = express();
const path = require('path');

const port = 3000;

app.use(express.static(path.join(__dirname, 'css')));
app.use(express.static(path.join(__dirname, 'js')));

app.get('/', (req, res) => {
  res.sendFile(path.join(__dirname, 'html', 'index.html'));
});


app.listen(port, () => {
  console.log(`Server running at http://localhost:${port}/`);
});
