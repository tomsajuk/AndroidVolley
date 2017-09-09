var express = require('express');
var router = express.Router();
var MongoClient = require('mongodb').MongoClient;

var app = express();

var url = "mongodb://localhost:27017/Basket";

router.get('/code/:barcode', function(req, res, next) {
	var code = parseInt(req.params.barcode);
	
	MongoClient.connect(url, function(err, db) {
	  if (err) throw err;
	  db.collection("Inventory").find({Barcode: code }, { _id: false}).toArray( function(err, result) {
	    if (err) throw err;

	    console.log(result);
	    res.json(result);
	    res.status(200);
	    res.end();
	    db.close();
	  });
	});
});




router.get('/name/:name', function(req, res, next) {
	var product_name = req.params.name;
	MongoClient.connect(url, function(err, db) {
	  if (err) throw err;
	  db.collection("Inventory").find({"Product Name":{$regex: product_name, $options:"$i" }}, { _id: false}).toArray( function(err, result) {
	    if (err) throw err;
	    console.log(result);

	    if(result.length == 0) {
	    	res.status(202);
	    }
	    else {

	    	var Inventory = [{"Product Name":result[0]["Product Name"] , "Unit Price": result[0]["Unit Price"]}];
		    var category = result[0].Category;

		    db.collection("Shelf").find({Category: category }, { _id: false}).toArray( function(err, result) {
			    if (err) throw err;
			    console.log(result);
			    var obj = [ {"Product Name":Inventory[0]["Product Name"],
			    			"Unit Price": Inventory[0]["Unit Price"],
			    			"Rack No": result[0]["Rack No"],
			    			"Shelf No": result[0]["Shelf No"]}];
			    console.log(obj);
			    
			    res.json(obj);
			    db.close();
			    res.status(200);
			    res.end();
			  });
		}
		
	  });
	});
});

module.exports = router;
