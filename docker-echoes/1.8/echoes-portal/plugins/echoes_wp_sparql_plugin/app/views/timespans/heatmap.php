<?php
         $url = $_SERVER['REQUEST_URI']; 
         if (strpos($url, '/ca/') !== false) {
                 $current_locale = '/ca';
         } elseif (strpos($url, '/nl/') !== false) {
                 $current_locale = '/nl';
         } elseif (strpos($url, '/fy/') !== false) {  
                 $current_locale = '/fy';
         } else {
                 $current_locale = '';
         }
?>
<style>
input[type="radio"] {
  margin-top: -1px;
  vertical-align: middle;
}
input {
    border: 1px solid #e0b13a;
    padding: 10px;
}
.google-visualization-tooltip {
  padding: 10px;
}
.google-visualization-tooltip > ul {
  list-style-type:none;
}

.dobleButton {
    position: relative !important;
    margin-top: -4px !important;
    right: -11px !important;
    padding-top: 64px;

}

@media screen and (min-width: 1200px) {
.subcontainer {
    margin-left: 0px;
    width: 0px;
}
}

input[type=date]::-webkit-inner-spin-button {
    -webkit-appearance: none;
    display: none;
}

.category-list {
    margin-top: 25px;
    margin-left: 5px;
}

.category {
    padding: 1em 0px 0px 53em;
}

.category-list span {
    margin-left: 32px;
    font-size: 15px;
}

.category-list a {
    text-decoration: none;
    color: black;
}

.category-list span img {
    width: 22px;
}

.title-browse {
    width: 285px;
    background-color: #26288E;
    position: absolute;
    height: 40px;
    color: white;
}

.categories img {
    margin-left: 35px;
    margin-top: 13px;
}

.categories h3 {
    margin-top: -18px;
    margin-left: 77px;
    font-size: large;
}

.people-list {
    list-style-type: none;
    margin-left: 0px;
    width: 33%;
    float: left;
}

.people-details {
    margin-left: 0px;
    margin-bottom: 10px;
    width: 18em;
    height: 5em;
}

.people-details-text {
    margin-left: 10px;
    margin-top: 10px;
}
</style>
<!--<link rel="stylesheet" type="text/css" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css"/>-->
<link rel="stylesheet" type="text/css" href="<?php echo mvc_css_url('echoes_wp_sparql_plugin', 'bootstrap.min.css') ?>"/>
<link rel="stylesheet" type="text/css" href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-datetimepicker/4.17.37/css/bootstrap-datetimepicker.css"/>
<script src="https://code.jquery.com/jquery-2.2.2.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/moment.js/2.10.3/moment.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-datetimepicker/4.17.37/js/bootstrap-datetimepicker.min.js"></script>
<h1 style="color: #27288e"><?php esc_html_e('Explore period of time','echoes_wp_sparql_plugin') ?></h1><br/>
<?php $type = ($_GET['type'] == 'agent') ? 1 : 0; $siteUrl = site_url(); $photoPath = get_bloginfo('template_url');?>
<div class="category" style="padding: 1em 0px 0px 67em !important;">
   <div class="title-browse" style="width:225px !important;">
      <span class="categories"><img src="<?= $photoPath ?>/img/Assets/ico-menu-categories.svg" /><h3><strong><?php esc_html_e('Filter by', 'echoes_wp_sparql_plugin')?></strong></h3></span>
       <div class="category-list" style="display: <?php echo ($type) ? 'none' : 'block'; ?>">
                <span><img src="<?= $photoPath ?>/img/Assets/ico-people-violet.svg" /><a style="text-decoration: none;" href="<?= $siteUrl . $current_locale ?>/timespans/heatmap/?date_start=<?= $date_start ?>&date_end=<?= $date_end ?>&page=1&type=agent">&nbsp;&nbsp;&nbsp;&nbsp;<?php esc_html_e('People', 'echoes_wp_sparql_plugin') ?></a></span>
       </div>
       <div class="category-list" style="display: <?php echo (!$type) ? 'none' : 'block'; ?>">
                <span><img src="<?= $photoPath ?>/img/Assets/ico-digital-objets-violet.svg" /><a style="text-decoration: none;" href="<?= $siteUrl . $current_locale ?>/timespans/heatmap/?date_start=<?= $date_start ?>&date_end=<?= $date_end ?>&page=1&type=cho">&nbsp;&nbsp;&nbsp;&nbsp;<?php esc_html_e('Digital Objects', 'echoes_wp_sparql_plugin') ?></a></span>
       </div>
   </div>
</div>
<div id="search-time">
<form id="searchform" style="width: 50%; height: 57px;" action="<?php echo WP_SITEURL; ?>/timespans/heatmap">
<input value="" id="searchsubmit" class="button avia-font-entypo-fontello dobleButton" style="margin-top: -9px !important;" type="submit">
<div style="max-width: 100%; margin-top: -20px!important;">
<div class="form-group" style="max-width: 40%">
   <div class="input-group date" style="width: 410px; position:absolute;">
	<input id="yearPicker" type="text" style="width: 47%; outline: 0; border; text-align: center; border: none; background-color: transparent; font-size: 2em; padding: 0px 0px 0px 0px;" name="date_start" <?php if (isset($_GET['date_start'])): ?>value="<?= $date_start ?>"<?php endif; ?>>
        <span style="width: 3%; padding-left: 1%; padding-right: 1%; font-weight: bolder"><?php esc_html_e('To', 'echoes_wp_sparql_plugin') ?></span>
   </div>
</div>
<div class="form-group" style="max-width: 40%; padding: 0em 0em 0em 21em;">
   <div class="input-group date" style="width: 115px; position:absolute;">
 	<input id="yearPickerEnd" type="text" style="width: 60%; text-align: center; border: none; background-color: transparent; font-size: 2em; padding: 0px 0px 0px 0px;" name="date_end" <?php if (isset($_GET['date_end'])): ?>value="<?= $date_end ?>"<?php endif; ?>>
   </div>
</div>
	<input type="hidden" name="page" value="1">
	<input type="hidden" name="type" value="<?= $_GET['type'] ?>">
</div>
</form>
</div>

<script type="text/javascript" src="https://www.gstatic.com/charts/loader.js"></script>
<!--
<?php if($type) : ?>
	<script src="<?php echo mvc_js_url('echoes_wp_sparql_plugin', 'timespans/agentsXday.js') ?>" type="text/javascript"></script>
<?php else : ?>
	<script src="<?php echo mvc_js_url('echoes_wp_sparql_plugin', 'timespans/chosXday.js') ?>" type="text/javascript"></script>
<?php endif ?>
-->
    <script type="text/javascript">

      $('#yearPicker').datetimepicker({
      	format      :   "YYYY",
    	viewMode    :   "years", 
      });

      $('#yearPickerEnd').datetimepicker({
        format      :   "YYYY",
        viewMode    :   "years", 
      });

      google.charts.load("current", {packages:["calendar"]});
      google.charts.setOnLoadCallback(drawChart);
      let start = <?php echo $date_start; ?> + (<?php echo $_GET['page']; ?> - 1) * 3;
      let end = (start + 3 > <?php echo $date_end; ?>) ? <?php echo $date_end; ?> : start + 2;
      let years = [];
      let heatmap = [];
      let title = (<?php echo $type ?> == 1) ? 'People' : 'Digital Objects';
      let list = (<?php echo $type ?> == 1) ? 'list_agents' : 'list_chos';
      let values=[];
      let queryType = (<?php echo $type ?> == 1) ? 'agent' : 'cho';

      $.ajax({
	      type: 'GET',
              async: false,
              timeout: 5*60*1000, //5m
              url: '/timespans/getValuesHeatmap',
              data: { 'type': queryType, 'start': start, 'end': end },
              contentType: "application/json; charset=utf-8",
              traditional: true,
              success: function (objects) {
	      		values = JSON.parse(objects);
              }
      });

      for (let heat of values){
	let year = parseInt(heat.year);
	if (year >= start && year <= end){
		years.push(year);
		for (let day of heat.info){
			heatmap.push(day);
		}
	}
      }

      let heatmap_trans = [];
      for (let date_sum of heatmap){
	let arrayDate = date_sum.days.replace(/-/g, ',').split(',');
        let date = new Date(parseInt(arrayDate[0]), parseInt(arrayDate[1]) - 1, parseInt(arrayDate[2]));
        heatmap_trans.push([date, parseInt(date_sum.count), "<h5 style='width:5em;'>" + parseInt(arrayDate[2]) + "-" + parseInt(arrayDate[1]) + "-" + parseInt(arrayDate[0]) + '</h5><b><?php esc_html_e('Total', 'echoes_wp_spaqrql_plugin')?></b>: ' + date_sum.count + '<br/><em><?php esc_html_e('Select to view', 'echoes_wp_sparql_plugin'); ?></em>']);
      }

   function drawChart() {
       var dataTable = new google.visualization.DataTable();
       dataTable.addColumn({ type: 'date', id: 'Date' });
       dataTable.addColumn({ type: 'number', id: 'Number' });
       dataTable.addColumn({ type: 'string', role: 'tooltip', 'p': {'html': true} });
       dataTable.addRows(heatmap_trans);
       var chart = new google.visualization.Calendar(document.getElementById('calendar_basic'));
       google.visualization.events.addListener(chart, 'onmouseover', function(e){
       });

       google.visualization.events.addListener(chart, 'select', function(){
         e = chart.getSelection();
          if (e.length > 0 && 'row' in e[0]){     
            let date = heatmap[e[0].row].days;
            $("#show_more_cho_content").load(WP_SITEURL + "/timespans/" + list + "/?date=" + date, function(){          
              $("#show_more_cho").slideDown();
            });      
          } else {
            $("#show_more_cho").slideUp();
          }
       });
       
       var options = {
         title: '<?php _e('People', 'echoes_wp_sparql_plugin'); ?>',
         height: 50 + 3 * 140,
         colorAxis: {minValue: 0,  colors: ['#e69cb8', '#f31869']},
         noDataPattern: {
  backgroundColor: 'white',
  color: 'white'
         },
calendar: {
    cellColor: {
      stroke: 'WhiteSmoke',      // Color the border of the squares.
      strokeWidth: 1      // ...and two pixels thick.
    },
      dayOfWeekLabel: {
        fontName: 'Times-Roman',
        fontSize: 12,
        color: '#27288e',
        bold: true
      },
      dayOfWeekRightSpace: 10,
      daysOfWeek: '<?php _e('SMTWTFS', 'echoes_wp_sparql_plugin') ?>',
      underYearSpace: 10, // Bottom padding for the year labels.
      yearLabel: {
        fontSize: 25,
        color: '#27288e',
        bold: true
      },
      monthLabel: {
        fontName: 'Times-Roman',
        fontSize: 12,
        color: '#27288e',
        bold: true
      }
  }
       };

        google.visualization.events.addListener(chart, 'ready', function(){
            
            $("text:contains('Jan')").text('<?php echo _e('Jan', 'echoes_wp_sparql_plugin') ?>');
            $("text:contains('Feb')").text('<?php echo _e('Feb', 'echoes_wp_sparql_plugin') ?>'); 
            $("text:contains('Mar')").text('<?php echo _e('Mar', 'echoes_wp_sparql_plugin') ?>'); 
            $("text:contains('Apr')").text('<?php echo _e('Apr', 'echoes_wp_sparql_plugin') ?>'); 
            $("text:contains('May')").text('<?php echo _e('May', 'echoes_wp_sparql_plugin') ?>'); 
            $("text:contains('Jun')").text('<?php echo _e('Jun', 'echoes_wp_sparql_plugin') ?>'); 
            $("text:contains('Jul')").text('<?php echo _e('Jul', 'echoes_wp_sparql_plugin') ?>'); 
            $("text:contains('Aug')").text('<?php echo _e('Aug', 'echoes_wp_sparql_plugin') ?>'); 
            $("text:contains('Sep')").text('<?php echo _e('Sep', 'echoes_wp_sparql_plugin') ?>'); 
            $("text:contains('Oct')").text('<?php echo _e('Oct', 'echoes_wp_sparql_plugin') ?>'); 
            $("text:contains('Nov')").text('<?php echo _e('Nov', 'echoes_wp_sparql_plugin') ?>'); 
            $("text:contains('Dec')").text('<?php echo _e('Dec', 'echoes_wp_sparql_plugin') ?>'); 
        });


       chart.draw(dataTable, options);
       	document.querySelector("#calendar_basic > div > div:nth-child(1) > div > svg > g:nth-child(2) > text:nth-child(4)").innerHTML = '<?php echo _e('Jan', 'echoes_wp_sparql_plugin') ?>';
       	document.querySelector("#calendar_basic > div > div:nth-child(1) > div > svg > g:nth-child(2) > text:nth-child(5)").innerHTML = '<?php echo _e('Feb', 'echoes_wp_sparql_plugin') ?>';
	document.querySelector("#calendar_basic > div > div:nth-child(1) > div > svg > g:nth-child(2) > text:nth-child(6)").innerHTML = '<?php echo _e('Mar', 'echoes_wp_sparql_plugin') ?>';
	document.querySelector("#calendar_basic > div > div:nth-child(1) > div > svg > g:nth-child(2) > text:nth-child(7)").innerHTML = '<?php echo _e('Apr', 'echoes_wp_sparql_plugin') ?>';
	document.querySelector("#calendar_basic > div > div:nth-child(1) > div > svg > g:nth-child(2) > text:nth-child(8)").innerHTML = '<?php echo _e('May', 'echoes_wp_sparql_plugin') ?>';
	document.querySelector("#calendar_basic > div > div:nth-child(1) > div > svg > g:nth-child(2) > text:nth-child(9)").innerHTML = '<?php echo _e('Jun', 'echoes_wp_sparql_plugin') ?>';
	document.querySelector("#calendar_basic > div > div:nth-child(1) > div > svg > g:nth-child(2) > text:nth-child(10)").innerHTML = '<?php echo _e('Jul', 'echoes_wp_sparql_plugin') ?>';
	document.querySelector("#calendar_basic > div > div:nth-child(1) > div > svg > g:nth-child(2) > text:nth-child(11)").innerHTML = '<?php echo _e('Aug', 'echoes_wp_sparql_plugin') ?>';
	document.querySelector("#calendar_basic > div > div:nth-child(1) > div > svg > g:nth-child(2) > text:nth-child(12)").innerHTML = '<?php echo _e('Sep', 'echoes_wp_sparql_plugin') ?>';
	document.querySelector("#calendar_basic > div > div:nth-child(1) > div > svg > g:nth-child(2) > text:nth-child(13)").innerHTML = '<?php echo _e('Oct', 'echoes_wp_sparql_plugin') ?>';
	document.querySelector("#calendar_basic > div > div:nth-child(1) > div > svg > g:nth-child(2) > text:nth-child(14)").innerHTML = '<?php echo _e('Nov', 'echoes_wp_sparql_plugin') ?>';
	document.querySelector("#calendar_basic > div > div:nth-child(1) > div > svg > g:nth-child(2) > text:nth-child(15)").innerHTML = '<?php echo _e('Dec', 'echoes_wp_sparql_plugin') ?>';
   }
    </script><br/><br/>
    <div class="subcontainer">
    <div id="calendar_basic" style="min-width: 950px; height: 100%; margin-top: 10px; float:left"></div>
    <div id="show_more_cho" style="float: left; margin-left: 20px; margin-top: 2em; display: none; width: 20%; min-width: 350px">
      <h4><?php $title = ($_GET['type'] == 'agent') ? __('People', 'echoes_wp_sparql_plugin') : __('Digital Objects', 'echoes_wp_sparql_plugin'); echo $title; ?></h4>
      <div id="show_more_cho_content" style="width: 313%;"></div>
      </div>
      <div style="width: 35em; margin-top: 5em; float:left; margin-left: 7em;">
	  <?php echo $this->pagination->show($num_pages); ?>
      </div>
    </div>
