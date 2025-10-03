const TotalBalances = ({total})=>{
    return(
        <>
            <div className="total-balance-content">
                <p>Total Balance</p>
                <div className="total-amount">
                    <p>GBP</p><span>{total}</span>
                </div>
            </div>
        </>
    );
}

export default TotalBalances;