export const formatCurrency = (value) => {
    return value.toLocaleString('en-US',{
        minimumFractionDigits: 2,
        maximumFractionDigits: 2
    })
}