<?php if ($changeTitle) : ?>
	<script>document.title = 'Echoes'</script> <!-- Es necesita per modificar el títol de la página, ja que la versió  del wordpress retorna un fals  -->
<?php endif; ?>
<div style='width: 70%; float: left'> 
    <?php 
    if (count($objects) > 0 && isset($order_items) && isset($order_entity) && isset($_GET['s']) && !empty($_GET['s'])): 
        $vars = ['order_entity' => $order_entity, 'order_items' => $order_items, 'order_data_value' => reset($order_items)['data-value']];
        if (isset($order_data_value)){
            $vars['order_data_value'] = $order_data_value;
        }
        //$this->render_view('general/order_button', ['locals' => $vars]); 
    endif; 
    ?>   
    <?php if (isset($search_title)): ?><br/><h3 class="entry-title"><?php echo $search_title; ?></h3><br/><?php endif; ?>
    <?php if (empty($objects)): ?>
        <h4>There are no results</h4>
    <?php else: ?>
        <?php echo $this->details->list_timespans($objects); ?>
    <?php endif ?>  
    <?php if (isset($extra_view)):
    if (isset($extra_view_vars)){
        $this->render_view($extra_view, ['locals' => $extra_view_vars]);
    } else {
        $this->render_view($extra_view);
    }    
    endif; ?>
    <div style="padding-top: 104em">
    <?php echo $this->pagination->show($num_pages); ?>
    <?php if (isset($print_legend)): ?>
        <div id="external_legend" style="background-color: #f9f9f9;margin-top: 15px">
            <?php echo $this->icons->print_legend(false, $agents_legend, $chos_legend, $cat_legend); ?>
        </div>
    <?php endif; ?>
    </div>
</div>
<div style='width: 25%; float: left; margin-left: 5%;'>
    <?php $this->details->search_bar($search_link); ?>
    <?php //$this->render_view('general/facets'); ?>
<?php $photoPath = get_bloginfo('template_url');
      $countItems = 15; 
      foreach($facets as $facet => $values) { ?>
	<div class="facets">
   	    <div class="title-browse">
      		<span class="categories"><img src="<?= $photoPath ?>/img/Assets/ico-menu-categories.svg" /><h3><strong><?= $facet ?></strong></h3></span>
		<?php foreach($values as $value) { 
		      	$value = (strlen($value) > 20) ? substr($value, 0, 20) . '...' : $value; ?>
        		<div class="facets-list">
                		<span><a href="">&nbsp;&nbsp;&nbsp;&nbsp;<?= $value ?>&nbsp;&nbsp;(<?= $countItems ?>)</a></span>
        		</div>
		<?php $countItems += 10; }  ?>
			<div class="facets-list">
				<span><a href="">&nbsp;&nbsp;&nbsp;&nbsp;Show more</a></span>
			</div>
           </div>
	</div>
<?php } ?>
    <?php /**$this->render_view('general/browse_by'); **/?>
    <?php /**$this->render_view('general/refine_results'); **/?> 
</div>
