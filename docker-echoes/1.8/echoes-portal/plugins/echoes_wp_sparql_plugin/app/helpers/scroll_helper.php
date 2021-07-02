<?php

class ScrollHelper extends MvcHelper {

  private $nextScroll;
  private $numPages;
  private $page;
  private $name;
  private $graph;
  private $total;
  private $place;
  private $type;
  private $year;
  private $years;
  private $typeDO;
  private $concept;
  private $yearsDeath;
  private $typeYear;

  public function __construct() {
	$this->page = 1;
  }

  public function setNextScroll($scrollId) {
	$this->nextScroll = $scrollId;
  }

  public function setNumPages($numPages) {
        $this->numPages = $numPages;
  }

  public function setPage($page) {
        $this->page = $page;
  }
  
  public function setName($name) {
        $this->name = $name;
  }

  public function setGraph($graph) {
        $this->graph = $graph;
  }

  public function setPlace($place) {
        $this->place = $place;
  }

  public function setTotal($total) {
        $this->total = $total;
  }

  public function setType($type) {
        $this->type = $type;
  }

  public function setYear($year) {
        $this->year = $year;
  }

  public function setYears($years) {
        $this->years = $years;
  }

  public function setTypeDO($typeDO) {
        $this->typeDO = $typeDO;
  }

  public function setConcept($concept) {
        $this->concept = $concept;
  } 

  public function setYearsDeath($yearsDeath) {
        $this->yearsDeath = $yearsDeath;
  }

  public function setTypeYear($typeYear) {
        $this->typeYear = $typeYear;
  }

  public function getNextScroll() {
        return $this->nextScroll;
  }
  
  public function getNumPages() {
        return $this->numPages;
  }

  public function getPage() {
        return $this->page;
  }

  public function getName() {
        return $this->name;
  }

  public function getGraph() {
        return $this->graph;
  }

  public function getPlace() {
        return $this->place;
  }

  public function getTotal() {
        return $this->total;
  }

  public function getType() {
        return $this->type;
  }

  public function getYear() {
        return $this->year;
  }

  public function getYears() {
        return $this->years;
  }

  public function getTypeDO() {
        return $this->typeDO;
  }

  public function getConcept() {
        return $this->concept;
  }

  public function getYearsDeath() {
        return $this->yearsDeath;
  }

  public function getTypeYear() {
        return $this->typeYear;
  }

  public function get_link_ret() {
      	$ret = $_SERVER['REQUEST_URI'];
    	return $ret;
  }

  public function updateScrollTime($graph, $type, $year, $typeDO, $concept, $typeYear) {
  	$this->graph = $graph;
	$this->year = $year;
	$this->type = $type;
	$this->typeDO = $typeDO;
	$this->concept = $concept;
	$this->typeYear = $typeYear;
  }

  public function updateScrollPeople($name, $graph, $years, $place, $concept, $type, $yearsDeath) {
        $this->name = $name;
        $this->graph = $graph;
        $this->place = $place;
        $this->years = $years;
	$this->concept = $concept;
	$this->yearsDeath = $yearsDeath;
	$this->type = $type;
  }

  public function updateScrollDO($place, $graph, $typeDO, $concept, $year) {
        $this->graphs = $graph;
        $this->place = $place;
	$this->concept = $concept;
	$this->typeDO = $typeDO;
	$this->year = $year;
  }

  public function show($serializeScroll) {
    $html = "<nav class='pagination scrollMargin'>
    <span class='pagination-meta'>" . sprintf(esc_html__('Page %1$d of %2$d', 'echoes_wp_sparql_plugin'), $this->page, $this->numPages) . "</span>"; 
    $html .= '<div style="margin-left:15px; margin-bottom:25px; width:5.5em;">';

       if ($this->page != 1):
        $html .= "<form style='float:left;' action='{$this->get_link_ret()}' method='post'>";
        $html .= "<input type='hidden' name='scroll' value='" . base64_encode($serializeScroll) . "' />";
	$html .= "<input type='hidden' name='move' value='back' />";
        $html .= "<input style='border:none; background-color:transparent;' type='submit' name='submit' value='<' />";
        $html .= "</form>";       
       endif;
    $html .= "<p style='float:left; margin-left:1em;'>{$this->page}</p>";
       if ($this->page != $this->numPages):
        $html .= "<form style='float:right;' action='{$this->get_link_ret()}' method='post'>";
	$html .= "<input type='hidden' name='scroll' value='" . base64_encode($serializeScroll) . "' />";
	$html .= "<input type='hidden' name='move' value='next' />";
	$html .= "<input style='border:none; background-color:transparent;' type='submit' name='submit' value='>' />";
	$html .= "</form>";
       endif;

  $html .= '</div>';
  $html .= '</nav>';
 return $html;

  }
}
