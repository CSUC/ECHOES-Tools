<?php if ($changeTitle) : ?>
	<script>document.title = 'Echoes'</script> <!-- Es necesita per modificar el títol de la página, ja que la versió  del wordpress retorna un fals  -->
<?php endif; ?>
<div class="row">
<div style='width: 70%; float: left'> 
    <?php 
    if (count($objects) > 0 && isset($order_items) && isset($order_entity) && isset($_GET['s']) && !empty($_GET['s'])): 
        $vars = ['order_entity' => $order_entity, 'order_items' => $order_items, 'order_data_value' => reset($order_items)['data-value']];
        if (isset($order_data_value)){
            $vars['order_data_value'] = $order_data_value;
        }
    endif; 
    ?>   
    <?php if (isset($search_title)): ?><br/><h3 class="entry-title"><?php echo $search_title; ?></h3><br/><?php endif; ?>
    <?php echo $facets->ShowDeleteOptionFacets(); ?>
    <?php if (empty($objects)): ?>
        <h4>There are no results</h4>
    <?php else: ?>
        <?php echo $this->details->list($objects); ?>
    <?php endif ?>  
    <?php if (isset($extra_view)):
    if (isset($extra_view_vars)){
        $this->render_view($extra_view, ['locals' => $extra_view_vars]);
    } else {
        $this->render_view($extra_view);
    }    
    endif; ?>
</div>
<div style='width: 25%; float: left; margin-left: 5%;'>
    <?php if($search_bar) {$this->details->search_bar();} ?>
    <?php if($search_barYear) {$this->details->search_barYear();} ?>
    <?php echo $facets->show($facets->getOptions()); ?>
    <?php if ($browse) {$this->render_view('general/browse_by');} ?>
</div>
</div>
</div>
    <div style="width: 70%">
        <?php echo $scroll->show($serializeScroll); ?>
        <?php if (isset($print_legend)): ?>
             <div id="external_legend" style="background-color: #f9f9f9;margin-top: 15px">
                <?php echo $this->icons->print_legend(false, $agents_legend, $chos_legend, $cat_legend); ?>
             </div>
        <?php endif; ?>
   </div>

