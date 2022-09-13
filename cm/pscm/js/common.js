var putThousandSeparate = function(num,type){
    const numberFormatter = Intl.NumberFormat('en-US');
    const formatted = numberFormatter.format(num);
    return formatted;
}
