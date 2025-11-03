class ContentUrlPlugin{
	constructor(){
	}

	readContent(url){
		return new Promise(function(resolve, reject){
			cordova.exec(
				function(result){
					resolve(result);
				},
				function(err){
					reject(err);
				},
				"ContentUrlPlugin", "readContent",
				[url]);
		});
	}
}

module.exports = new ContentUrlPlugin();
