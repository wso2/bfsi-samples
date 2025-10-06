import './doughnut-chart.css'
import {useEffect, useState} from "react";
import {Doughnut} from "react-chartjs-2";
import {ArcElement, Chart as ChartJS, Legend, Tooltip} from "chart.js";

ChartJS.register(ArcElement, Tooltip, Legend);

const DoughnutChart = ({bankInfoAndTotals})=>{

    const [chartData, setChartData] = useState(null);

    useEffect(() => {
        if (!bankInfoAndTotals || bankInfoAndTotals.length === 0) {
            setChartData(null);
            return;
        }

        const bankNames = [];
        const totals = [];
        const fillColors = [];
        const borderColors = [];

        bankInfoAndTotals.forEach(bankInfo => {
            bankNames.push(bankInfo.name);
            totals.push(bankInfo.totalBalance);
            fillColors.push(bankInfo.color);
            borderColors.push(bankInfo.border);
        });

        const newChartData = {
            labels: bankNames,
            datasets: [
                {
                    label: 'Total Balance',
                    data: totals,
                    backgroundColor: fillColors,
                    borderColor: borderColors,
                    borderWidth: 2,
                    cutout: '75%',
                },
            ],
        };
        setChartData(newChartData);
    }, [bankInfoAndTotals]);

    const options = {
        responsive: true,
        maintainAspectRatio: false,
        plugins: {
            legend: {
                position: 'right', // Place legend on the side
                align: 'start',
                labels: {
                    usePointStyle: true,
                    boxWidth: 8,
                    padding: 10,
                }
            },
            tooltip: {
                // Ensure tooltips are enabled
            }
        },
    };
    if (!chartData) {
        return <div style={{height: '100%', display: 'flex', alignItems: 'center', justifyContent: 'center', color: '#ccc'}}>Loading Chart Data...</div>;
    }

    return (
        <div style={{ height: '100%', width: '100%', display: 'flex', flexDirection: 'column', padding: '1rem' }}>
            <h2 style={{fontSize: '1.2rem', margin: '0 0 1rem', color: '#333'}}>Account Distribution</h2>
            <div style={{ flexGrow: 1, minHeight: 0 }}>
                <Doughnut data={chartData} options={options} />
            </div>
        </div>
    );
}

export default DoughnutChart;