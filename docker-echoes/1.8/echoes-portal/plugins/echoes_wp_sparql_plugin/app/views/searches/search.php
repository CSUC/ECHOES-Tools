<!DOCTYPE html>
<?php if ($changeTitle) : ?>
        <script>document.title = 'Echoes'</script> <!-- Es necesita per modificar el títol de la página, ja que la versió  del wordpress retorna un fals  -->
<?php endif; ?>
<?php if (isset($search_title)): ?><br/><h3 class="entry-title"><?php echo $search_title; ?></h3><br/><?php endif; ?>
<html>
<head>
<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
</head>
<div style='width: 90%;' class="container" id="results"></div>
    <div style="text-align:center; width: 90%;">
	<?php if ($type != 'name') : ?>
    		<input type="button" id="first" onclick="firstPage()" value="first" />
    		<input type="button" id="next" onclick="nextPage()" value="next" />
    		<input type="button" id="previous" onclick="previousPage()" value="previous" />
    		<input type="button" id="last" onclick="lastPage()" value="last" />
	<?php else : ?>
		<input type="button" id="next" onclick="loadList('next', true)" value="next" />
                <input type="button" id="previous" onclick="loadList('previous', true)" value="previous" />
	<?php endif; ?>
    </div>
<script>
    	let list = new Array();
	let response = new Array();
    	let pageList = new Array();
	let fullList = new Array();
	let keys = new Array();
	let indexKey;
	let key;
    	let currentPage = 1;
    	let numberOfPages = 0;
        let type = "<?php echo $type; ?>";
        let url = "<?php echo $url; ?>";
	let name = "<?php echo $nameFacet; ?>";
	let concept = "<?php echo $conceptFacet; ?>";
	let year = "<?php echo $yearFacet; ?>";
	let years = "<?php echo $yearsFacet; ?>";
	let place = "<?php echo $placeFacet; ?>";
	let graph = "<?php echo $graphFacet; ?>";
	let typeDO = "<?php echo $typeDOFacet; ?>";
	let yearsDeath = "<?php echo $yearsDeathFacet; ?>";
	let numberPerPage = (type =='name') ? 90 : 75;
	
	_searchAllFacets('next');

        function _searchAllFacets(move){	
		if(type == 'name') { key = keys[indexKey];}
		$.ajax({
        		type: 'GET',
			async: false,
			timeout: 5*60*1000, //5m
                	url: '/searches/getAllOptionsFacets',
                	data: {	'type': type, 'url': url, 'afterKey': key, 'nameFacet': name, 'conceptFacet': concept, 'yearFacet': year, 'yearsFacet': years, 'placeFacet': place, 'graphFacet': graph, 'typeDOFacet': typeDO , 'yearsDeath': yearsDeath},
                	contentType: "application/json; charset=utf-8",
                    	success: function (objects) {
				  response = JSON.parse(objects);
				  console.log(response);
				  $.each(response, function(k, v) {
    					list.push(Array(v['html']));
  				  });
				  keys.push(response[0]['afterKey']);
                    	}
                }).fail(function(e){ console.log(e);});
	}
	
	function getNumberOfPages() {
    		return Math.ceil(list.length / numberPerPage);
	}

	function nextPage() {
    		currentPage += 1;
    		loadList();
	}

	function previousPage() {
    		currentPage -= 1;
    		loadList();
	}

	function firstPage() {
    		currentPage = 1;
    		loadList();
	}

	function lastPage() {
    		currentPage = numberOfPages;
    		loadList();
	}

	function loadList(move, isLoop) {

		if(isLoop){ 
			list = [];

			if(indexKey == null){
				indexKey = 0;
			}else{ 
				(move == 'next') ? indexKey++ : indexKey--;
			}

			_searchAllFacets(move); 
		}

    		var begin = ((currentPage - 1) * numberPerPage);
    		var end = begin + numberPerPage;
    		pageList = list.slice(begin, end);
    		drawList();
    		(type == 'name') ? checkIfIsName() : check();
	}
    
	function drawList() {
		let count = 0;
    		let html = '<div class="row">';
    		for (r = 0; r < pageList.length; r++) {
                        if(count >= 3){
                                html += '</div>';
                                html += '<div class="row">';
                                count = 0;
                        }
                        html += "<div class='col-sm people-details'>" + pageList[r] + "</div>";
                        count++;
    		}
                html += '</div>';
                $('#results').html(html);
	}

	function check() {
    		document.getElementById("next").disabled = currentPage == numberOfPages ? true : false;
    		document.getElementById("previous").disabled = currentPage == 1 ? true : false;
    		document.getElementById("first").disabled = currentPage == 1 ? true : false;
    		document.getElementById("last").disabled = currentPage == numberOfPages ? true : false;
	}

        function checkIfIsName() {
                document.getElementById("next").disabled = response[0]['afterKey'] == null ? true : false;
                document.getElementById("previous").disabled = (indexKey == -1 || indexKey == null) ? true : false;
        }

	numberOfPages = getNumberOfPages();
	window.onload = loadList('next', false);

</script>
