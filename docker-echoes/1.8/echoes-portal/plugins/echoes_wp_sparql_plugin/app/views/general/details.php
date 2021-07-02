<div style='width: 100%; float: left'> 
    <?php if (isset($extra_view)):
    if (isset($extra_view_vars)){
        $this->render_view($extra_view, ['locals' => $extra_view_vars]);
    } else {
        $this->render_view($extra_view);
    }    
    endif; ?>  
    <?php if (isset($print_legend)): ?>
        <div id="external_legend" style="background-color: #f9f9f9;margin-top: 15px">
            <?php echo $this->icons->print_legend(false, $agents_legend, $chos_legend, $cat_legend); ?>
        </div>
    <?php endif; ?>
</div>
