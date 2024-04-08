document.addEventListener("DOMContentLoaded", () => {
    const urlId = document.getElementById("urlId").value;

    fetch(`/api/shortenedUrls/${urlId}`)
        .then(response => response.json())
        .then(shortLink => {
            browsersGraph(shortLink);
            osGraph(shortLink);
            datesGraph(shortLink.visitorDates);
            iptable(shortLink.ipCounts);
        })
        .catch(error => console.log(error));
});

function browsersGraph(shortLink) {
    const browserCounts = shortLink.visitorList ? shortLink.browserCounts : {};
    const ctx = document.getElementById('browsersChart');
    if (browserCounts === null || Object.keys(browserCounts).length === 0) {
        ctx.parentElement.innerHTML = "<h1>No data available</h1>";
    } else {
        const backgroundColors = Object.keys(browserCounts).map(() => {
            const r = Math.floor(Math.random() * 256);
            const g = Math.floor(Math.random() * 256);
            const b = Math.floor(Math.random() * 256);
            return `rgb(${r}, ${g}, ${b})`;
        });

        const data = {
            labels: Object.keys(browserCounts),
            datasets: [{
                label: 'Visits',
                data: Object.values(browserCounts),
                backgroundColor: backgroundColors,
                hoverOffset: 4
            }]
        };

        const config = {
            type: 'doughnut',
            data: data,
        };

        new Chart(ctx, config);
    }
}

function osGraph(shortLink) {
    const osCounts = shortLink.visitorList ? shortLink.osCounts : {};
    const ctx = document.getElementById('osChart');
    if (osCounts === null || Object.keys(osCounts).length === 0) {
        ctx.parentElement.innerHTML = "<h1>No data available</h1>";
    } else {
        const backgroundColors = Object.keys(osCounts).map(() => {
            const r = Math.floor(Math.random() * 256);
            const g = Math.floor(Math.random() * 256);
            const b = Math.floor(Math.random() * 256);
            return `rgb(${r}, ${g}, ${b})`;
        });

        const data = {
            labels: Object.keys(osCounts),
            datasets: [{
                label: 'Visits',
                data: Object.values(osCounts),
                backgroundColor: backgroundColors,
                hoverOffset: 4
            }]
        };

        const config = {
            type: 'doughnut',
            data: data,
        };

        new Chart(ctx, config);
    }
}

function datesGraph(dates) {
    const ctx = document.getElementById('datesChart');
    if (dates === null || dates.length === 0) {
        ctx.parentElement.innerHTML = "<h1>No data available</h1>";
    } else {
        const dateLabels = dates.map(date => new Date(date).toLocaleString());
        const visitCounts = {};

        for (let i = 0; i < dates.length; i++) {
            const date = new Date(dates[i]);
            const dateStr = date.toDateString();
            const hourStr = date.getHours();
            const key = `${dateStr} ${hourStr}:00`;
            if (key in visitCounts) {
                visitCounts[key]++;
            } else {
                visitCounts[key] = 1;
            }
        }

        const data = {
            labels: Object.keys(visitCounts),
            datasets: [{
                label: 'Visits',
                data: Object.values(visitCounts),
                fill: true,
                borderColor: 'rgb(75, 192, 192)',
                tension: 0.1
            }]
        };

        const config = {
            type: 'line',
            data: data,
            options: {
                scales: {
                    y: {
                        beginAtZero: true
                    }
                }
            }
        };

        new Chart(ctx, config);
    }
}

function iptable(ipCount) {
    const table = document.getElementById('ipsTable');
    const tbody = table.querySelector("tbody");
    if (ipCount === null || Object.keys(ipCount).length === 0) {
        table.parentElement.innerHTML = "<h1>No data available</h1>";
    } else {
        console.log(Object.keys(ipCount).length);
        Object.keys(ipCount).forEach( key => {
            const row = document.createElement("tr");
            row.innerHTML = `<td>${key}</td> <td>${ipCount[key]}</td>`
            tbody.append(row);
        })

    }
}



