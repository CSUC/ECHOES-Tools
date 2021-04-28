function obj(object, index){
    return object[i(object, index)];
}

function i(object, index){
    return Object.keys(object)[index];
}
function len(object){
    return Object.keys(object).length;
}

function add3Dots(string, limit)
{
  var dots = "...";
  if(string.length > limit)
  {
    // you can also use substr instead of substring
    string = string.substring(0,limit) + dots;
  }

    return string;
}