function transform(val){
     if(!val) {return ''};

	console.log("length="+val.length);

     if(val.length === 11) {
     	return val.substr(0,3)+"-"+val.substr(3,4)+"-"+val.substr(7,4);
    }

    return val;
  }
        
  console.log(transform('13673979325'));
