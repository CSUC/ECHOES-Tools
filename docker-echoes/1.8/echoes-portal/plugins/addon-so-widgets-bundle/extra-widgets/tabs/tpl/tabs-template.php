<?php
$widget_title =  wp_kses_post($instance['widget_title']);
$tabs_selection =  wp_kses_post($instance['tabs_selection']);
$tab_icon_position =  wp_kses_post($instance['tabs_styling']['tab_icon_position']);
?>


<?php if ($widget_title) { ?>
    <h3 class="widget-title">
        <span><?php echo $widget_title ?></span>
    </h3>
<?php } ?>


<?php if($tabs_selection == 'horizontal'): ?>

<div class="soua-tab">

    <ul class="soua-tabs">
        <?php foreach( $instance['repeater'] as $i => $repeater ) : ?>
        <li><a href="#"> 

        <?php if($tab_icon_position == 'top'):?>    
        <?php if(siteorigin_widget_get_icon( $repeater['tab_icon'])): ?>
        <div class="tab-icon-top">
        <?php echo siteorigin_widget_get_icon( $repeater['tab_icon'] ); ?>
        </div>
        <?php endif; ?>
        <?php endif; ?>

        <?php if($tab_icon_position == 'left'):?>    
        <?php if(siteorigin_widget_get_icon( $repeater['tab_icon'])): ?>
        <div class="tab-icon-left">
        <?php echo siteorigin_widget_get_icon( $repeater['tab_icon'] ); ?>
        </div>
        <?php endif; ?>
        <?php endif; ?>

        <?php echo $repeater['tab_title']; ?>

        <?php if($tab_icon_position == 'right'):?>   
        <?php if(siteorigin_widget_get_icon( $repeater['tab_icon'])): ?>
        <div class="tab-icon-right">
        <?php echo siteorigin_widget_get_icon( $repeater['tab_icon'] ); ?>
        </div>
        <?php endif; ?>
        <?php endif; ?>

        </a></li>
        <?php endforeach; ?>
    </ul> <!-- / tabs -->

    <div class="tab_content">
        <?php foreach( $instance['repeater'] as $i => $repeater ) : ?>
        <div class="tabs_item">
            <p><?php echo $repeater['tab_content'] ?></p>
        </div> <!-- / tabs_item -->
        <?php endforeach; ?>

    </div> <!-- / tab_content -->
</div> <!-- / tab -->

<?php elseif($tabs_selection == 'vertical'): ?>


    <div class="soua-tab vertical">

        <ul class="soua-tabs ">
            <?php foreach( $instance['repeater'] as $i => $repeater ) : ?>
                <li><a href="#"> 

                <?php if($tab_icon_position == 'top'):?>    
                <?php if(siteorigin_widget_get_icon( $repeater['tab_icon'])): ?>
                <div class="tab-icon-top">
                <?php echo siteorigin_widget_get_icon( $repeater['tab_icon'] ); ?>
                </div>
                <?php endif; ?>
                <?php endif; ?>

                <?php if($tab_icon_position == 'left'):?>    
                <?php if(siteorigin_widget_get_icon( $repeater['tab_icon'])): ?>
                <div class="tab-icon-left">
                <?php echo siteorigin_widget_get_icon( $repeater['tab_icon'] ); ?>
                </div>
                <?php endif; ?>
                <?php endif; ?>

                <?php echo $repeater['tab_title']; ?>

                <?php if($tab_icon_position == 'right'):?>   
                <?php if(siteorigin_widget_get_icon( $repeater['tab_icon'])): ?>
                <div class="tab-icon-right">
                <?php echo siteorigin_widget_get_icon( $repeater['tab_icon'] ); ?>
                </div>
                <?php endif; ?>
                <?php endif; ?>

                </a></li>
            <?php endforeach; ?>
        </ul> <!-- / tabs -->

        <div class="tab_content">
            <?php foreach( $instance['repeater'] as $i => $repeater ) : ?>
                <div class="tabs_item">
                    <p><?php echo $repeater['tab_content'] ?></p>
                </div> <!-- / tabs_item -->
            <?php endforeach; ?>

        </div> <!-- / tab_content -->
    </div> <!-- / tab -->



<?php endif; ?>