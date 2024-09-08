const button = document.getElementById('start');
button.addEventListener('click', () => {
    fetch('https://127.0.0.1/demo/charge/startCharge')
       .then(response => response.json())
       .then(data => {
            console.log(data);
        })
       .catch(error => {
            console.error('Error fetching data:', error);
        });
});